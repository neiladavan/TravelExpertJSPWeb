<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Rewards</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/dayjs@1/dayjs.min.js"></script>
</head>
<body>
<h1><%= "Customer Rewards" %>
</h1>
<button id="addCustomerReward">Add</button>
<table id="customerRewardsTable">
    <tbody>
        <th>Reward Card</th>
        <th>Reward Number</th>
    </tbody>
</table>
<button id="saveBtn">Save</button>
<br/>

<!-- Add Customer Reward Modal -->
<div id="addCustomerRewardModal" style="display: none;">
    <div class="modal-content">
        <h2>Add Customer Reward</h2>
        <form id="addCustomerRewardForm">
            <label for="rewardSelect">Reward:</label>
            <select id="rewardSelect" required>
                <!-- Rewards will be populated dynamically -->
            </select>

            <label for="rwdNumber">Reward Number:</label>
            <input type="text" id="rwdNumber" />

            <button type="submit">Add Customer Reward</button>
            <button type="button" id="closeModal">Cancel</button>
        </form>
    </div>
</div>

<script src="./customer_rewards.js" defer></script>
</body>
</html>