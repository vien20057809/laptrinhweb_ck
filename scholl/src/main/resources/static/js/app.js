const App = {
    baseUrl: window.location.origin,

    escapeHtml(str) {
        return String(str ?? "")
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;");
    },

    escapeAttr(str) {
        return String(str ?? "")
            .replace(/&/g, "&amp;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#39;");
    },

    setLoading(show) {
        let el = document.getElementById("loadingOverlay");
        if (!el) {
            el = document.createElement("div");
            el.id = "loadingOverlay";
            el.className = "loading-overlay";
            el.innerHTML = '<div class="spinner-ring"></div>';
            document.body.appendChild(el);
        }
        el.classList.toggle("show", !!show);
    },

    toast(message, type = "success") {
        let host = document.getElementById("toastHost");
        if (!host) {
            host = document.createElement("div");
            host.id = "toastHost";
            document.body.appendChild(host);
        }
        const t = document.createElement("div");
        t.className = `app-toast ${type}`;
        t.textContent = message;
        host.appendChild(t);
        setTimeout(() => t.remove(), 4000);
    },

    showBanner(id, message) {
        const el = document.getElementById(id);
        if (!el) return;
        el.textContent = message;
        el.classList.add("show", "error");
    },

    hideBanner(id) {
        const el = document.getElementById(id);
        if (!el) return;
        el.classList.remove("show", "error");
        el.textContent = "";
    },

    async parseError(res) {
        try {
            const data = await res.json();
            if (data.errors) {
                return Object.values(data.errors).join(". ");
            }
            return data.message || "Yêu cầu thất bại";
        } catch {
            return "Yêu cầu thất bại";
        }
    },

    async fetchJson(url, options = {}) {
        App.setLoading(true);
        try {
            const res = await fetch(url, options);
            if (!res.ok) {
                let data = {};
                try {
                    data = await res.json();
                } catch {
                    /* ignore */
                }
                const err = new Error(data.message || "Yêu cầu thất bại");
                err.errors = data.errors;
                throw err;
            }
            if (res.status === 204) return null;
            return res.json();
        } finally {
            App.setLoading(false);
        }
    },

    setActiveNav(page) {
        document.querySelectorAll(".app-menu a[data-page]").forEach((a) => {
            a.classList.toggle("active", a.dataset.page === page);
        });
    },

    formatDateTime(iso) {
        if (!iso) return "—";
        const d = new Date(iso);
        if (Number.isNaN(d.getTime())) return App.escapeHtml(iso);
        return d.toLocaleString("vi-VN");
    },

    toLocalInputValue(iso) {
        if (!iso) return "";
        const d = new Date(iso);
        if (Number.isNaN(d.getTime())) return "";
        const pad = (n) => String(n).padStart(2, "0");
        return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
    },

    fromLocalInputValue(value) {
        if (!value) return null;
        return new Date(value).toISOString().slice(0, 19);
    },

    formatDate(iso) {
        if (!iso) return "—";
        const d = new Date(iso);
        if (Number.isNaN(d.getTime())) return App.escapeHtml(iso);
        return d.toLocaleDateString("vi-VN");
    },

    toDateInputValue(iso) {
        if (!iso) return "";
        const d = new Date(iso);
        if (Number.isNaN(d.getTime())) return "";
        const pad = (n) => String(n).padStart(2, "0");
        return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
    },

    fromDateInputValue(value) {
        if (!value) return null;
        return value;
    }
};
