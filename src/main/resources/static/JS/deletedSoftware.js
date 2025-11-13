const API_BASE = "http://localhost:8082";
let deletedList = [];
let selectedId = null;
let selectedAction = null;

// Load deleted software
async function loadDeletedSoftware() {
    try {
        const res = await fetch(`${API_BASE}/api/software/all`);
        if (!res.ok) throw new Error("Failed to load data");
        const data = await res.json();
        deletedList = data.filter(s => s.isDeleted === true);
        renderTable(deletedList);
    } catch (err) {
        console.error(err);
        document.getElementById("deleted-message").innerText = "❌ मेटिएका डेटा लोड गर्न समस्या भयो!";
    }
}

// Render table
function renderTable(list) {
    const tbody = document.querySelector("#deletedSoftwareTable tbody");
    tbody.innerHTML = "";

    if (list.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" style="text-align:center;">⚠️ कुनै मेटिएका सफ्टवेयर छैन</td></tr>`;
        return;
    }

    list.forEach(s => {
        const row = `
            <tr>
                <td>${s.id}</td>
                <td>${s.softwareName}</td>
                <td>${s.department}</td>
				<td>${s.inputer}</td>
                <td>${s.lastUpdated || 'N/A'}</td>
                <td>
				<a href="/software/details?id=${s.id}" class="action-btn view-btn" title="View">
								                    <i class="fa-solid fa-eye"></i>
								                </a>
                    <button class="action-btn restore-btn" onclick="showActionModal(${s.id}, 'restore')">Restore</button>
                    <button class="action-btn delete-permanent-btn" onclick="showActionModal(${s.id}, 'delete')">Delete Permanently</button>
                </td>
            </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", row);
    });
}

// Modal handling
function showActionModal(id, action) {
    selectedId = id;
    selectedAction = action;
    const msg = action === "restore" 
        ? "Do you want to restore this software?" 
        : "Do you want to permanently delete this software?";
    document.getElementById("actionModalMessage").textContent = msg;
    const btn = document.getElementById("confirmActionBtn");
    btn.textContent = action === "restore" ? "Restore" : "Delete Permanently";
    document.getElementById("actionModal").classList.add("active");
}

function closeActionModal() {
    document.getElementById("actionModal").classList.remove("active");
    selectedId = null;
    selectedAction = null;
}

// Confirm action
async function confirmAction() {
    if (!selectedId || !selectedAction) return;

    try {
        if (selectedAction === "restore") {
            const softRes = await fetch(`${API_BASE}/api/software/${selectedId}`);
            if (!softRes.ok) throw new Error("Software not found");
            const software = await softRes.json();
            software.isDeleted = false;

            // Use FormData for multipart/form-data
            const formData = new FormData();
            for (const key in software) {
                if (software[key] !== null && software[key] !== undefined) {
                    formData.append(key, software[key]);
                }
            }

            const saveRes = await fetch(`${API_BASE}/api/software/save`, {
                method: "POST",
                body: formData
            });

            if (!saveRes.ok) throw new Error("Restore failed");
            showSuccess("Software restored successfully!");
        } else {
            const delRes = await fetch(`${API_BASE}/api/software/delete/${selectedId}`, { method: 'DELETE' });
            if (!delRes.ok) throw new Error("Delete failed");
            showSuccess("Software permanently deleted!");
        }

        closeActionModal();
        loadDeletedSoftware();
    } catch (err) {
        console.error(err);
        alert("❌ Action failed!");
    }
}

// Success modal
function showSuccess(message) {
    document.getElementById("successModalMessage").textContent = message;
    document.getElementById("successModal").classList.add("active");
}
function closeSuccess() {
    document.getElementById("successModal").classList.remove("active");
}

// Event listeners
document.getElementById("confirmActionBtn").addEventListener("click", confirmAction);
document.getElementById("cancelActionBtn").addEventListener("click", closeActionModal);
document.getElementById("actionCloseBtn").addEventListener("click", closeActionModal);
document.getElementById("successCloseBtn").addEventListener("click", closeSuccess);
document.getElementById("successOkBtn").addEventListener("click", closeSuccess);

window.onload = loadDeletedSoftware;
