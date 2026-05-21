# Interactive Task Manager

## Project Description

A feature-rich task management application built with vanilla JavaScript. It includes task CRUD operations, filtering, search, localStorage persistence, drag and drop ordering, backup and restore, task statistics, validation, and a responsive dark/light interface.

## Project Overview and Objectives

The goal of this Week 2 project is to practice JavaScript fundamentals through a complete browser application. The project demonstrates variables, functions, classes, arrays, objects, DOM manipulation, event handling, form validation, and localStorage.

## Features

- Add, edit, delete, and complete tasks
- Filter tasks by All, Active, and Completed
- Search tasks by text
- Save tasks in localStorage
- Restore tasks automatically on page load
- Clear completed tasks
- Backup and restore task data with JSON files
- Drag and drop task reordering
- Due dates and priority levels
- Dark and light mode toggle
- Live task statistics
- Responsive layout for mobile and desktop

## How to Use

1. Open `index.html` in a browser.
2. Enter a task title in the input field.
3. Select an optional due date and priority.
4. Press Enter or click Add Task.
5. Use the checkbox to mark a task complete.
6. Click the edit button or task text to update a task.
7. Click the delete button to remove a task.
8. Use the filter buttons to switch between task views.
9. Use Backup and Restore to export or import JSON task data.
10. Press `/` to focus search and Escape to clear search or cancel editing.

## Technologies Used

- HTML5
- CSS3
- Vanilla JavaScript
- DOM APIs
- localStorage
- FileReader and Blob APIs

## Setup Instructions

1. Download or clone the repository.
2. Open the `week2-task-manager` folder.
3. Open `index.html` directly in a browser.
4. Add a few tasks and refresh the page to verify localStorage persistence.

No package installation is required.

## Project Structure

```text
week2-task-manager/
|-- index.html
|-- css/
|   |-- style.css
|   `-- theme.css
|-- js/
|   |-- app.js
|   |-- storage.js
|   |-- ui.js
|   `-- utils.js
|-- README.md
`-- .gitignore
```

## Code Structure

- `index.html` contains the application layout, task form, filter controls, statistics, list container, and task template.
- `css/theme.css` stores color variables for light and dark themes.
- `css/style.css` contains responsive layouts, task cards, buttons, form states, animations, and accessibility helpers.
- `js/utils.js` contains reusable helpers for IDs, text cleanup, dates, JSON downloads, and JSON file reading.
- `js/storage.js` handles task and theme persistence with localStorage.
- `js/ui.js` controls DOM rendering, form messages, statistics, and theme display.
- `js/app.js` contains the main TaskManager class, event handling, array operations, filtering, editing, drag and drop, backup, restore, and persistence.

## Technical Details

Tasks are stored as an array of objects. Each task object includes an ID, text, completed state, due date, priority, and creation date. Array methods such as `map`, `filter`, `find`, `findIndex`, `unshift`, and `splice` are used for task management.

The application updates the DOM without page reloads. Each change updates the task array, saves it to localStorage, and re-renders the filtered task list. The UI uses a template element so every rendered task has the same accessible structure.

## Testing Evidence

- Added a valid task and confirmed it appears immediately.
- Tried submitting fewer than three characters and confirmed validation appears.
- Edited task text, due date, and priority successfully.
- Marked tasks complete and verified statistics update.
- Switched between All, Active, and Completed filters.
- Searched for task text and confirmed matching results appear.
- Deleted a task and confirmed localStorage updates.
- Cleared completed tasks.
- Refreshed the page and confirmed tasks persist.
- Exported a JSON backup and restored it.
- Reordered tasks with drag and drop.
- Switched between dark and light themes.
- Checked the layout on desktop and mobile widths.

## Visual Documentation

Add screenshots of the working application to the submission if required by your instructor. Recommended screenshots include the empty state, task list with filters, edit workflow, dark mode, and mobile layout.

## Quality Standards Checklist

- [x] Project overview and objectives included
- [x] Setup instructions included
- [x] Code structure documented
- [x] Technical details explained
- [x] Testing evidence included
- [x] DOM manipulation implemented
- [x] Event handling implemented
- [x] Array methods used for task management
- [x] localStorage persistence implemented
- [x] Form validation and error handling included
- [x] Dynamic updates without page reload included
- [x] Responsive and user-friendly interface included
