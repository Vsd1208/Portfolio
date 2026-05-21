class TaskUI {
    constructor(manager) {
        this.manager = manager;
        this.elements = {
            form: document.getElementById("taskForm"),
            input: document.getElementById("taskInput"),
            dueDate: document.getElementById("dueDate"),
            priority: document.getElementById("priority"),
            submitButton: document.getElementById("submitButton"),
            error: document.getElementById("taskError"),
            search: document.getElementById("searchInput"),
            taskList: document.getElementById("taskList"),
            template: document.getElementById("taskTemplate"),
            emptyState: document.getElementById("emptyState"),
            status: document.getElementById("statusMessage"),
            filters: [...document.querySelectorAll(".filter-button")],
            total: document.getElementById("totalTasks"),
            active: document.getElementById("activeTasks"),
            completed: document.getElementById("completedTasks"),
            highPriority: document.getElementById("highPriorityTasks"),
            clearCompleted: document.getElementById("clearCompleted"),
            backup: document.getElementById("backupTasks"),
            restore: document.getElementById("restoreTasks"),
            themeToggle: document.getElementById("themeToggle"),
            themeIcon: document.getElementById("themeIcon")
        };
    }

    render(tasks) {
        this.elements.taskList.replaceChildren(...tasks.map(task => this.createTaskNode(task)));
        this.elements.emptyState.classList.toggle("hidden", tasks.length > 0);
        this.updateStats();
    }

    createTaskNode(task) {
        const node = this.elements.template.content.firstElementChild.cloneNode(true);
        const check = node.querySelector(".task-check");
        const text = node.querySelector(".task-text");
        const priority = node.querySelector(".priority-pill");
        const due = node.querySelector(".due-pill");

        node.dataset.id = task.id;
        node.classList.toggle("completed", task.completed);
        check.checked = task.completed;
        text.textContent = task.text;
        priority.textContent = task.priority;
        priority.classList.add(task.priority);
        due.textContent = TaskUtils.formatDate(task.dueDate);

        return node;
    }

    updateStats() {
        const stats = this.manager.getStats();
        this.elements.total.textContent = stats.total;
        this.elements.active.textContent = stats.active;
        this.elements.completed.textContent = stats.completed;
        this.elements.highPriority.textContent = stats.highPriority;
    }

    setFilter(filter) {
        this.elements.filters.forEach(button => {
            button.classList.toggle("active", button.dataset.filter === filter);
        });
    }

    setTheme(theme) {
        document.documentElement.dataset.theme = theme;
        this.elements.themeIcon.textContent = theme === "dark" ? "☀" : "☾";
    }

    showError(message) {
        this.elements.error.textContent = message;
    }

    clearError() {
        this.elements.error.textContent = "";
    }

    showStatus(message) {
        this.elements.status.textContent = message;
        window.clearTimeout(this.statusTimer);
        this.statusTimer = window.setTimeout(() => {
            this.elements.status.textContent = "";
        }, 2200);
    }

    resetForm() {
        this.elements.form.reset();
        this.elements.priority.value = "medium";
        this.elements.input.focus();
    }
}
