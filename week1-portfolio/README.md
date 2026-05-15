# Personal Portfolio Website

## Project Description

A responsive personal portfolio website for Vandanapu Sai Dhiraj, showcasing skills, projects, education, and contact information. The website is built with HTML5, CSS3, JavaScript, and responsive design principles so it works across desktop, tablet, and mobile screen sizes.

## Project Overview and Objectives

The objective of this project is to create a clean and professional personal portfolio that presents academic background, technical skills, selected projects, and contact details in one accessible website.

The portfolio is designed to:

- Introduce the student with a clear hero section.
- Present education and academic performance.
- Display technical skills in organized categories.
- Showcase selected projects with visual UI previews.
- Provide contact information and a working validated contact form.
- Demonstrate semantic HTML, responsive CSS layouts, and basic JavaScript interactivity.

## Features

- Responsive design for mobile, tablet, and desktop screens
- Semantic HTML5 structure
- CSS Grid and Flexbox layouts
- Accessible navigation
- Contact form with validation
- Hover effects and animations
- Project cards with UI mockup images
- Profile, education, skills, projects, and contact sections

## Technologies Used

- HTML5
- CSS3
- JavaScript for basic interactivity
- Git for version control

## Setup and Installation Instructions

1. Download or clone the repository.
2. Open the `week1-portfolio` folder in VS Code.
3. Open `index.html` directly in a web browser.
4. Optional: use the VS Code Live Server extension for automatic browser refresh while editing.

No package installation is required because the project uses plain HTML, CSS, and JavaScript.

## Project Structure

```text
week1-portfolio/
|-- index.html
|-- css/
|   |-- variables.css
|   |-- style.css
|   `-- responsive.css
|-- js/
|   `-- navigation.js
|-- images/
|   |-- icons/
|   `-- projects/
|       |-- aqi-dashboard.svg
|       |-- carbon-cloud.svg
|       |-- ticket-chatbot.svg
|       `-- voice-translator.svg
|-- screenshots/
|   |-- 01-home.png
|   |-- 02-about.png
|   |-- 03-skills.png
|   |-- 04-projects-top.png
|   |-- 05-projects-bottom.png
|   `-- 06-contact.png
|-- README.md
`-- .gitignore
```

## Code Structure Explanation

- `index.html` contains the semantic page structure, navigation, hero section, about section, skills section, projects section, contact form, and footer.
- `css/variables.css` stores reusable design values such as colors, spacing, radius values, shadows, and font settings.
- `css/style.css` contains the main visual styling for layout, cards, buttons, hero visuals, project cards, forms, and footer.
- `css/responsive.css` contains media queries that adjust the layout for tablet and mobile screens.
- `js/navigation.js` controls the mobile navigation menu and validates the contact form fields before submission.
- `images/projects/` contains project UI mockup images used in the project cards.
- `images/icons/` contains social media icons used in the footer.

## Screenshots of Working Application

Place the screenshots in the `screenshots/` folder using the filenames below. The screenshots should be ordered according to the page flow.

### 1. Home Section

![Home section](screenshots/01-home.png)

Shows the navigation bar, hero introduction, call-to-action buttons, profile visual, and quick stats.

### 2. About Section

![About section](screenshots/02-about.png)

Shows the profile summary and education card with degree, university, duration, and CGPA.

### 3. Skills Section

![Skills section](screenshots/03-skills.png)

Shows skills grouped into Languages, Libraries, and Tools & Concepts.

### 4. Projects Section - First Row

![Projects section first row](screenshots/04-projects-top.png)

Shows the AQI Prediction System and Voice to Text Translator project cards.

### 5. Projects Section - Second Row

![Projects section second row](screenshots/05-projects-bottom.png)

Shows the CIAVMP Implementation System and Voice Based Ticket Booking System project cards.

### 6. Contact Section

![Contact section](screenshots/06-contact.png)

Shows contact details and the validated contact form.

## How Technical Requirements Were Met

### Responsive Design

The website uses responsive CSS media queries in `css/responsive.css` to adjust layout for desktop, tablet, and mobile screens. Grid sections collapse from multi-column layouts into single-column layouts on smaller screens.

### Semantic HTML5 Structure

The page uses semantic elements such as `header`, `nav`, `main`, `section`, `article`, `address`, and `footer`. These elements improve accessibility, readability, and structure.

### CSS Grid and Flexbox Layouts

CSS Grid is used for major page layouts such as the hero, about, skills, projects, and contact sections. Flexbox is used for navigation, buttons, tag lists, footer links, and grouped content alignment.

### Accessible Navigation

The navigation includes ARIA labels and a mobile menu button with `aria-expanded`. Keyboard users can open and close the menu, and the Escape key closes the mobile navigation.

### Contact Form Validation

The contact form uses JavaScript validation in `js/navigation.js`. It checks that the name has at least two characters, the email is valid, and the message has at least ten characters before showing a success message.

### Hover Effects and Animations

Buttons, cards, navigation links, and project cards include hover and focus states. Transitions are used to make interactions feel smooth, while reduced-motion support is included for users who prefer minimal animation.

### Visual Assets

The project cards use SVG UI mockups stored in `images/projects/`. These images make the projects section more professional while keeping the website lightweight.

## Testing Evidence

- Navigation links move to the correct page sections.
- Mobile navigation opens, closes, and responds to the Escape key.
- Contact form blocks invalid input and displays validation messages.
- Layout adapts for desktop, tablet, and mobile widths.
- Project images load from local SVG files.
- Focus states are visible for keyboard accessibility.

## Quality Standards Checklist

- [x] Project overview and objectives included
- [x] Setup and installation instructions included
- [x] Code structure explained
- [x] Screenshots section included and ordered
- [x] Technical requirements explained
- [x] Responsive design implemented
- [x] Semantic HTML5 used
- [x] CSS Grid and Flexbox used
- [x] Accessible navigation included
- [x] Contact form validation included
- [x] Hover effects and animations included
