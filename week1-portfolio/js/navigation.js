const navToggle = document.querySelector(".nav-toggle");
const navLinks = document.querySelector(".nav-links");
const contactForm = document.querySelector("#contact-form");
const formStatus = document.querySelector("#form-status");

const closeMenu = () => {
    navToggle.classList.remove("is-open");
    navLinks.classList.remove("is-open");
    navToggle.setAttribute("aria-expanded", "false");
    navToggle.setAttribute("aria-label", "Open navigation menu");
};

navToggle.addEventListener("click", () => {
    const isOpen = navToggle.classList.toggle("is-open");
    navLinks.classList.toggle("is-open", isOpen);
    navToggle.setAttribute("aria-expanded", String(isOpen));
    navToggle.setAttribute("aria-label", isOpen ? "Close navigation menu" : "Open navigation menu");
});

navLinks.addEventListener("click", (event) => {
    if (event.target.matches("a")) {
        closeMenu();
    }
});

document.addEventListener("keydown", (event) => {
    if (event.key === "Escape" && navLinks.classList.contains("is-open")) {
        closeMenu();
        navToggle.focus();
    }
});

const validators = {
    name(value) {
        return value.trim().length >= 2 ? "" : "Please enter at least two characters.";
    },
    email(value) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value.trim()) ? "" : "Please enter a valid email address.";
    },
    message(value) {
        return value.trim().length >= 10 ? "" : "Please enter a message of at least ten characters.";
    }
};

const setFieldError = (field, message) => {
    const error = document.querySelector(`#${field.id}-error`);
    field.setAttribute("aria-invalid", message ? "true" : "false");
    field.setAttribute("aria-describedby", error.id);
    error.textContent = message;
};

const validateField = (field) => {
    const message = validators[field.name](field.value);
    setFieldError(field, message);
    return !message;
};

contactForm.addEventListener("input", (event) => {
    if (event.target.matches("input, textarea")) {
        validateField(event.target);
        formStatus.textContent = "";
    }
});

contactForm.addEventListener("submit", (event) => {
    event.preventDefault();
    const fields = Array.from(contactForm.querySelectorAll("input, textarea"));
    const isValid = fields.every(validateField);

    if (!isValid) {
        formStatus.textContent = "Please fix the highlighted fields.";
        fields.find((field) => field.getAttribute("aria-invalid") === "true").focus();
        return;
    }

    formStatus.textContent = "Thanks! Your message is ready to send.";
    contactForm.reset();
    fields.forEach((field) => field.setAttribute("aria-invalid", "false"));
});
