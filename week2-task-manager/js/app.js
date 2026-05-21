class TaskManager {
    constructor() {
        this.tasks = TaskStorage.loadTasks();
        this.currentFilter = "all";
        this.searchTerm = "";
        this.editingId = null;
        this.draggingId = null;
        this.ui = new TaskUI(this);
        this.init();
    }

    init() {
        this.ui.setTheme(TaskStorage.loadTheme());
        this.setupEventListeners();
        this.renderTasks();
    }

    setupEventListeners() {
        this.ui.elements.form.addEventListener("submit", event => {
            event.preventDefault();
            this.handleSubmit();
        });

        this.ui.elements.input.addEventListener("input", () => this.ui.clearError());

        this.ui.elements.filters.forEach(button => {
            button.addEventListener("click", () => {
                this.currentFilter = button.dataset.filter;
                this.ui.setFilter(this.currentFilter);
                this.renderTasks();
            });
        });

        this.ui.elements.search.addEventListener("input", event => {
            this.searchTerm = event.target.value.toLowerCase();
            this.renderTasks();
        });

        this.ui.elements.taskList.addEventListener("click", event => this.handleListClick(event));
        this.ui.elements.taskList.addEventListener("dblclick", event => this.handleListDoubleClick(event));
        this.ui.elements.taskList.addEventListener("dragstart", event => this.handleDragStart(event));
        this.ui.elements.taskList.addEventListener("dragover", event => this.handleDragOver(event));
        this.ui.elements.taskList.addEventListener("drop", event => this.handleDrop(event));
        this.ui.elements.taskList.addEventListener("dragend", event => this.handleDragEnd(event));

        this.ui.elements.clearCompleted.addEventListener("click", () => this.clearCompleted());
        this.ui.elements.backup.addEventListener("click", () => this.backupTasks());
        this.ui.elements.restore.addEventListener("change", event => this.restoreTasks(event));
        this.ui.elements.themeToggle.addEventListener("click", () => this.toggleTheme());

        document.addEventListener("keydown", event => {
            if (event.key === "/" && document.activeElement !== this.ui.elements.input) {
                event.preventDefault();
                this.ui.elements.search.focus();
            }

            if (event.key === "Escape") {
                this.cancelEdit();
                this.ui.elements.search.value = "";
                this.searchTerm = "";
                this.renderTasks();
            }
        });
    }

    handleSubmit() {
        const text = TaskUtils.cleanText(this.ui.elements.input.value);
        const dueDate = this.ui.elements.dueDate.value;
        const priority = this.ui.elements.priority.value;

        if (text.length < 3) {
            this.ui.showError("Enter at least 3 characters.");
            return;
        }

        if (this.editingId) {
            this.updateTask(this.editingId, { text, dueDate, priority });
            this.cancelEdit();
            this.ui.showStatus("Task updated.");
        } else {
            this.addTask(text, dueDate, priority);
            this.ui.showStatus("Task added.");
        }

        this.ui.resetForm();
    }

    addTask(text, dueDate, priority) {
        this.tasks.unshift({
            id: TaskUtils.createId(),
            text,
            completed: false,
            dueDate,
            priority,
            createdAt: new Date().toISOString()
        });
        this.commit();
    }

    updateTask(id, updates) {
        this.tasks = this.tasks.map(task => task.id === id ? { ...task, ...updates } : task);
        this.commit();
    }

    deleteTask(id) {
        this.tasks = this.tasks.filter(task => task.id !== id);
        this.commit();
        this.ui.showStatus("Task deleted.");
    }

    toggleComplete(id) {
        this.tasks = this.tasks.map(task => task.id === id ? { ...task, completed: !task.completed } : task);
        this.commit();
    }

    startEdit(id) {
        const task = this.tasks.find(item => item.id === id);

        if (!task) {
            return;
        }

        this.editingId = id;
        this.ui.elements.input.value = task.text;
        this.ui.elements.dueDate.value = task.dueDate || "";
        this.ui.elements.priority.value = task.priority;
        this.ui.elements.submitButton.textContent = "Save Task";
        this.ui.elements.input.focus();
    }

    cancelEdit() {
        this.editingId = null;
        this.ui.elements.submitButton.textContent = "Add Task";
        this.ui.clearError();
    }

    clearCompleted() {
        const before = this.tasks.length;
        this.tasks = this.tasks.filter(task => !task.completed);
        this.commit();
        this.ui.showStatus(before === this.tasks.length ? "No completed tasks to clear." : "Completed tasks cleared.");
    }

    backupTasks() {
        TaskUtils.downloadJson("task-manager-backup.json", this.tasks);
        this.ui.showStatus("Backup downloaded.");
    }

    async restoreTasks(event) {
        const file = event.target.files[0];

        if (!file) {
            return;
        }

        try {
            const restored = await TaskUtils.readJsonFile(file);
            if (!Array.isArray(restored)) {
                throw new Error("Invalid backup");
            }
            this.tasks = restored
                .filter(task => task && typeof task.text === "string")
                .map(task => ({
                    id: task.id || TaskUtils.createId(),
                    text: TaskUtils.cleanText(task.text),
                    completed: Boolean(task.completed),
                    dueDate: task.dueDate || "",
                    priority: ["low", "medium", "high"].includes(task.priority) ? task.priority : "medium",
                    createdAt: task.createdAt || new Date().toISOString()
                }));
            this.commit();
            this.ui.showStatus("Tasks restored.");
        } catch {
            this.ui.showStatus("Restore failed. Choose a valid JSON backup.");
        } finally {
            event.target.value = "";
        }
    }

    toggleTheme() {
        const current = document.documentElement.dataset.theme === "dark" ? "dark" : "light";
        const next = current === "dark" ? "light" : "dark";
        TaskStorage.saveTheme(next);
        this.ui.setTheme(next);
    }

    getFilteredTasks() {
        return this.tasks.filter(task => {
            const matchesFilter = this.currentFilter === "all" || (this.currentFilter === "active" && !task.completed) || (this.currentFilter === "completed" && task.completed);
            const matchesSearch = task.text.toLowerCase().includes(this.searchTerm);
            return matchesFilter && matchesSearch;
        });
    }

    getStats() {
        const total = this.tasks.length;
        const completed = this.tasks.filter(task => task.completed).length;
        const highPriority = this.tasks.filter(task => task.priority === "high" && !task.completed).length;
        return {
            total,
            completed,
            active: total - completed,
            highPriority
        };
    }

    handleListClick(event) {
        const item = event.target.closest(".task-item");

        if (!item) {
            return;
        }

        const id = item.dataset.id;

        if (event.target.closest(".task-check")) {
            this.toggleComplete(id);
        }

        if (event.target.closest(".delete-task")) {
            this.deleteTask(id);
        }

        if (event.target.closest(".edit-task") || event.target.closest(".task-text")) {
            this.startEdit(id);
        }
    }

    handleListDoubleClick(event) {
        const item = event.target.closest(".task-item");

        if (item && event.target.closest(".task-text")) {
            this.startEdit(item.dataset.id);
        }
    }

    handleDragStart(event) {
        const item = event.target.closest(".task-item");

        if (!item) {
            return;
        }

        this.draggingId = item.dataset.id;
        item.classList.add("dragging");
        event.dataTransfer.effectAllowed = "move";
    }

    handleDragOver(event) {
        event.preventDefault();
    }

    handleDrop(event) {
        event.preventDefault();
        const target = event.target.closest(".task-item");

        if (!target || !this.draggingId || target.dataset.id === this.draggingId) {
            return;
        }

        const fromIndex = this.tasks.findIndex(task => task.id === this.draggingId);
        const toIndex = this.tasks.findIndex(task => task.id === target.dataset.id);
        const moved = this.tasks.splice(fromIndex, 1)[0];
        this.tasks.splice(toIndex, 0, moved);
        this.commit();
    }

    handleDragEnd(event) {
        const item = event.target.closest(".task-item");

        if (item) {
            item.classList.remove("dragging");
        }

        this.draggingId = null;
    }

    commit() {
        TaskStorage.saveTasks(this.tasks);
        this.renderTasks();
    }

    renderTasks() {
        this.ui.render(this.getFilteredTasks());
    }
}

const taskManager = new TaskManager();
