let rewards = [];
let customerRewards = [];

function getRewardsAndCustomerRewards() {
    // Fetch and store rewards.
    const rewardsFetch = fetch("http://localhost:8080/TravelExpertsREST_war_exploded/api/rewards")
        .then(response => response.json())
        .then(data => {
            rewards = data;
        });
    // Fetch and populate customer rewards list
    const customerRewardsFetch = fetch("http://localhost:8080/TravelExpertsREST_war_exploded/api/customer-reward/get/104")
        .then(response => response.json())
        .then(data => {
            customerRewards = data;
        });

    Promise.all([rewardsFetch, customerRewardsFetch])
        .then(() => {
            console.log("Rewards fetched.");
            populateCustomerRewardsTable(customerRewards); // Call after both fetches complete
        })
        .catch(error => {
            console.error("Error fetching data:", error);
            alert("Failed to fetch rewards.");
        });
}

document.addEventListener("DOMContentLoaded", () => {
    getRewardsAndCustomerRewards();

    // Add customer reward button functionality
    document.getElementById("addCustomerReward").addEventListener("click", () => {
        populateRewardsSelect();
        document.getElementById("addCustomerRewardModal").style.display = "block";
    });

    // Close modal functionality
    document.getElementById("closeModal").addEventListener("click", () => {
        document.getElementById("addCustomerRewardModal").style.display = "none";
    });

    document.getElementById("addCustomerRewardForm").addEventListener("submit", handleAddCustomerReward);

    document.getElementById("saveBtn").addEventListener("click", saveCustomerRewards);
});

function populateCustomerRewardsTable(customerRewards) {
    const tableBody = document.querySelector("#customerRewardsTable > tbody");
    tableBody.innerHTML =
        "<th>Reward Card</th>" +
        "<th>Reward Number</th>";
    customerRewards.forEach(customerReward => {
        const row = createCustomerRewardRow(customerReward);
        tableBody.appendChild(row);
    });
}

function createCustomerRewardRow(customerReward){
    const row = document.createElement("tr");
    const associatedReward = rewards.find(reward => reward.id === customerReward.rewardId);
    const rewardName = associatedReward ? associatedReward.rwdName : "N/A";
    row.innerHTML = `
        <td>${rewardName}</td>
        <input value="${customerReward.rwdNumber}" onchange="updateCustomerRewardRwdNumber(${customerReward.rewardId}, this.value)">
        <td>
            <button class="deleteCustomerReward">Delete</button>
        </td>
        `;
    row.querySelector(".deleteCustomerReward").addEventListener("click", () => {
        deleteCustomerReward(customerReward.customerId, customerReward.rewardId);
        row.remove();
    });
    return row;
}

function updateCustomerRewardRwdNumber(rewardId, newRwdNumber) {
    const customerRewardToUpdate = customerRewards.find(cr => cr.rewardId === rewardId);
    if (customerRewardToUpdate) {
        customerRewardToUpdate.rwdNumber = newRwdNumber;
    }
}
function saveCustomerRewards() {
    // Prepare the payload
    const payload = customerRewards.map(customerReward => ({
        rewardId: customerReward.rewardId,
        customerId: customerReward.customerId,
        rwdNumber: customerReward.rwdNumber,
    }));
    fetch("http://localhost:8080/TravelExpertsREST_war_exploded/api/customer-reward/post", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    }).then(response => {
        if (response.ok) alert("Customer rewards saved!");
    });
}

function deleteCustomerReward(customerId, rewardId) {
    fetch(`http://localhost:8080/TravelExpertsREST_war_exploded/api/customer-reward/delete/${customerId}/${rewardId}`,
        { method: "DELETE" })
        .then(response => {
            return response.json();
        })
        .then(data =>  {
            console.log("API Response", data);
            customerRewards = customerRewards.filter(customerReward => customerReward.rewardId !== rewardId);
        })
        .catch(error => {
            console.error("Error saving customer reward:", error);
            alert("Failed to save customer reward.");
        });

    populateRewardsSelect();
}

function populateRewardsSelect() {
    const rewardSelect = document.getElementById("rewardSelect");
    rewardSelect.innerHTML = ""; // Clear existing options

    // Create a set of reward IDs already used by customer rewards
    const usedRewardIds = new Set(customerRewards.map(customerReward => customerReward.rewardId));

    // Filter rewards and add options to the dropdown
    rewards
        .filter(reward => !usedRewardIds.has(reward.id)) // Exclude used rewards
        .forEach(reward => {
            const option = document.createElement("option");
            option.value = reward.id;
            option.textContent = reward.rwdName;
            rewardSelect.appendChild(option);
        });

    // If no rewards are available, disable the dropdown
    if (rewardSelect.options.length === 0) {
        const option = document.createElement("option");
        option.value = "";
        option.textContent = "No rewards available";
        rewardSelect.appendChild(option);
        rewardSelect.disabled = true;
    } else {
        rewardSelect.disabled = false;
    }
}

function handleAddCustomerReward() {
    event.preventDefault();

    const rwdId = document.getElementById("rewardSelect").value;
    const parsedRwdId = Number(rwdId);
    const rwdNumber = document.getElementById("rwdNumber").value;

    const newCustomerReward = { rewardId: parsedRwdId, customerId: 104, rwdNumber: rwdNumber };
    // Send the new widget to the backend
    fetch("http://localhost:8080/TravelExpertsREST_war_exploded/api/customer-reward/post", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newCustomerReward),
    })
        .then(response => response.json())
        .then(data => {
            console.log(data.customerReward);
            if(data.customerReward) {
                // Add the saved customer reward to the global array
                customerRewards.push(data.customerReward);

                // Add the new customer reward to the table
                const tableBody = document.querySelector("#customerRewardsTable tbody");
                const newRow = createCustomerRewardRow(data.customerReward);
                tableBody.appendChild(newRow);

                // Repopulate the rewards dropdown
                populateRewardsSelect();

                // Close the modal
                document.getElementById("addCustomerRewardModal").style.display = "none";
            }
        })
        .catch(error => {
            console.error("Error saving customer reward:", error);
            alert("Failed to save customer reward.");
        });
}