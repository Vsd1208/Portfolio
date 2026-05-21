const TaskStorage = {
    taskKey: "week2-task-manager.tasks",
    themeKey: "week2-task-manager.theme",
    loadTasks() {
        try {
            const saved = JSON.parse(localStorage.getItem(this.taskKey));
            return Array.isArray(saved) ? saved : [];
        } catch {
            return [];
        }
    },
    saveTasks(tasks) {
        localStorage.setItem(this.taskKey, JSON.stringify(tasks));
    },
    loadTheme() {
        return localStorage.getItem(this.themeKey) || "light";
    },
    saveTheme(theme) {
        localStorage.setItem(this.themeKey, theme);
    }
};
