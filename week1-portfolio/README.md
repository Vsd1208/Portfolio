# Vandanapu Sai Dhiraj Portfolio Website

## Project Description

A responsive personal portfolio website for Vandanapu Sai Dhiraj, showcasing education, technical skills, projects, and contact information. Built with semantic HTML5, organized CSS3, and basic JavaScript interactivity.

## Features

- Responsive design for mobile, tablet, and desktop screens
- Semantic HTML5 structure with `header`, `nav`, `main`, `section`, and `footer`
- CSS Grid and Flexbox layouts
- Accessible navigation with ARIA attributes and keyboard support
- Contact form with client-side validation
- Hover, focus, and transition states for interactive elements
- Project UI mockup images with local social icons
- CV-based profile content, project summaries, and contact links

## Technologies Used

- HTML5
- CSS3
- JavaScript
- Git for version control

## Project Structure

```text
week1-portfolio/
|-- index.html
|-- css/
|   |-- style.css
|   |-- responsive.css
|   `-- variables.css
|-- js/
|   `-- navigation.js
|-- images/
|   |-- projects/
|   |   |-- aqi-dashboard.svg
|   |   |-- carbon-cloud.svg
|   |   |-- ticket-chatbot.svg
|   |   `-- voice-translator.svg
|   `-- icons/
|-- README.md
`-- .gitignore
```

## Setup Instructions

1. Open the `week1-portfolio` folder in VS Code.
2. Install helpful extensions such as Live Server, HTML CSS Support, and Prettier.
3. Open `index.html` directly in a browser, or right-click it in VS Code and choose "Open with Live Server".
4. Edit the name, contact details, skills, project descriptions, and social links in `index.html` if future CV details change.

## Code Structure

- `index.html` contains the full semantic page structure and Sai Dhiraj's portfolio details.
- `css/variables.css` defines reusable colors, fonts, spacing, shadows, and sizing values.
- `css/style.css` contains base, layout, component, form, and interaction styles.
- `css/responsive.css` contains mobile and tablet media queries.
- `js/navigation.js` controls the mobile menu and validates the contact form.

## Technical Details

The layout uses a mobile-first responsive approach with Flexbox for navigation and action groups, and CSS Grid for the hero, skills, projects, and contact sections. JavaScript toggles the mobile navigation state through `aria-expanded` and validates form fields before showing a success message.

## Testing Evidence

- Confirmed semantic page sections are present.
- Confirmed navigation links jump to their matching sections.
- Confirmed contact form blocks empty, short, and invalid email submissions.
- Confirmed responsive breakpoints adapt the layout for mobile, tablet, and desktop.
- Confirmed visible focus styles are available for keyboard users.

## Quality Standards Checklist

- [x] Clear project overview
- [x] Setup instructions
- [x] Organized file structure
- [x] Visual assets included
- [x] Technical details documented
- [x] Testing notes included
- [x] Semantic HTML5
- [x] External CSS files
- [x] Responsive media queries
- [x] Contact form validation
- [x] Accessible navigation
