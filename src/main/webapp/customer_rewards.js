let rewards = [];
let customerRewards = [];

async function getRewardsAndCustomerRewards() {
    try {
        // $.get() is shorthand for HTTP GET request.
        const rewardsResponse = await $.get("http://localhost:8080/TravelExpertsREST_war_exploded/api/rewards");
        const customerRewardsResponse = await $.get("http://localhost:8080/TravelExpertsREST_war_exploded/api/customer-reward/get/104");

        rewards = rewardsResponse;
        customerRewards = customerRewardsResponse;

        console.log("Rewards fetched.");
        populateCustomerRewardsTable(customerRewards); // Call after both fetches complete
    } catch (error) {
        console.error("Error fetching data:", error);
        alert("Failed to fetch rewards.");
    }
}

$(document).ready(() => {
    getRewardsAndCustomerRewards();

    // Add customer reward button functionality
    $("#addCustomerReward").click(() => {
        populateRewardsSelect();
        $("#addCustomerRewardModal").show();
    });

    // Close modal functionality
    $("#closeModal").click(() => {
        $("#addCustomerRewardModal").hide();
    });

    $("#addCustomerRewardForm").submit(handleAddCustomerReward);

    $("#saveBtn").click(saveCustomerRewards);
});

function populateCustomerRewardsTable(customerRewards) {
    const tableBody = $("#customerRewardsTable > tbody");
    tableBody.empty().append("<th>Reward Card</th><th>Reward Number</th>");

    customerRewards.forEach(customerReward => {
        const row = createCustomerRewardRow(customerReward);
        tableBody.append(row);
    });
}

function createCustomerRewardRow(customerReward) {
    const associatedReward = rewards.find(reward => reward.id === customerReward.rewardId);
    const rewardName = associatedReward ? associatedReward.rwdName : "N/A";

    // wrapped in jQuery object for easy manipulation, like .find()
    const row = $(`
        <tr>
            <td>${rewardName}</td>
            <td><input value="${customerReward.rwdNumber}" onchange="updateCustomerRewardRwdNumber(${customerReward.rewardId}, this.value)"></td>
            <td><button class="deleteCustomerReward">Delete</button></td>
        </tr>
    `);

    row.find(".deleteCustomerReward").click(() => {
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
    $.ajax({
        url: "http://localhost:8080/TravelExpertsREST_war_exploded/api/customer-reward/put/batch/",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(customerRewards),
        success: () => alert("Customer rewards saved!"),
        error: (error) => {
            console.error("Error saving customer rewards:", error);
            alert("Failed to save customer rewards.");
        }
    });
}

function deleteCustomerReward(customerId, rewardId) {
    $.ajax({
        url: `http://localhost:8080/TravelExpertsREST_war_exploded/api/customer-reward/delete/${customerId}/${rewardId}`,
        method: "DELETE",
        success: () => {
            customerRewards = customerRewards.filter(customerReward => customerReward.rewardId !== rewardId);
            populateRewardsSelect();
        },
        error: (error) => {
            console.error("Error deleting customer reward:", error);
            alert("Failed to delete customer reward.");
        }
    });
}

function populateRewardsSelect() {
    const rewardSelect = $("#rewardSelect");
    rewardSelect.empty();

    const usedRewardIds = new Set(customerRewards.map(customerReward => customerReward.rewardId));

    rewards
        .filter(reward => !usedRewardIds.has(reward.id))
        .forEach(reward => {
            rewardSelect.append(`<option value="${reward.id}">${reward.rwdName}</option>`);
        });

    if (rewardSelect.children().length === 0) {
        rewardSelect.append('<option value="">No rewards available</option>').prop("disabled", true);
    } else {
        rewardSelect.prop("disabled", false);
    }
}

function handleAddCustomerReward(event) {
    event.preventDefault();

    const rwdId = Number($("#rewardSelect").val());
    const rwdNumber = $("#rwdNumber").val();
    const newCustomerReward = { rewardId: rwdId, customerId: 104, rwdNumber: rwdNumber };

    $.ajax({
        url: "http://localhost:8080/TravelExpertsREST_war_exploded/api/customer-reward/post",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(newCustomerReward),
        success: (data) => {
            if (data.customerReward) {
                customerRewards.push(data.customerReward);
                const newRow = createCustomerRewardRow(data.customerReward);
                $("#customerRewardsTable tbody").append(newRow);
                populateRewardsSelect();
                $("#addCustomerRewardModal").hide();
            }
        },
        error: (error) => {
            console.error("Error saving customer reward:", error);
            alert("Failed to save customer reward.");
        }
    });
}
