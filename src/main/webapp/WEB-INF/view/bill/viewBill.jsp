<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="../shared/layout.jsp"%>
<!-- Main Content -->
<div class="px-8 py-6">
	<!-- Page Header -->
	<div class="flex items-center justify-between mb-6">
		<div>
			<h1 class="text-3xl font-bold text-gray-900">Bill Details</h1>
			<p class="text-gray-600 mt-1">View and manage bill information</p>
		</div>
		<a href="bill?action=list"
			class="bg-gray-100 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-200 transition-colors duration-200 flex items-center">
			<i class="fas fa-list mr-2"></i>Back to Bills
		</a>
	</div>

	<!-- Alerts -->
	<c:if test="${not empty success}">
		<div class="mb-6 bg-emerald-50 border border-emerald-200 rounded-xl p-4">
			<div class="flex items-center">
				<i class="fas fa-check-circle text-green-500 mr-2"></i> <span
					class="text-green-800">${success}</span>
			</div>
		</div>
	</c:if>

	<c:if test="${not empty error}">
		<div class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4">
			<div class="flex items-center">
				<i class="fas fa-exclamation-circle text-red-500 mr-2"></i> <span
					class="text-red-800">${error}</span>
			</div>
		</div>
	</c:if>

	<!-- Bill Details Card -->
	<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
		<!-- Bill Header Information -->
		<div
			class="grid grid-cols-1 md:grid-cols-2 gap-6 border-b border-gray-200 pb-6 mb-6">
			<!-- Customer Information -->
			<div>
				<h3
					class="text-lg font-semibold text-gray-900 mb-4 flex items-center">
					<i class="fas fa-user mr-2 text-cyan-600"></i>Customer
					Information
				</h3>
				<div class="space-y-2">
					<p class="text-gray-700">
						<i class="fas fa-user-circle mr-2 text-gray-500"></i> <strong>Name:</strong>
						${bill.customerName}
					</p>
					<p class="text-gray-700">
						<i class="fas fa-hashtag mr-2 text-gray-500"></i> <strong>Account
							Number:</strong> ${bill.accountNumber}
					</p>
					<p class="text-gray-700">
						<i class="fas fa-file-invoice mr-2 text-gray-500"></i> <strong>Bill
							ID:</strong> ${bill.billId}
					</p>
					<p class="text-gray-700">
						<i class="fas fa-calendar mr-2 text-gray-500"></i> <strong>Date:</strong>
						${bill.billDate}
					</p>
				</div>
			</div>

			<!-- Bill Status -->
			<div class="text-right md:text-left">
				<h3
					class="text-lg font-semibold text-gray-900 mb-4 flex items-center">
					<i class="fas fa-info-circle mr-2 text-cyan-600"></i>Bill Status
				</h3>
				<div class="mb-4">
					<span
						class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium capitalize
                            ${bill.status == 'paid' ? 'bg-green-100 text-green-700' : ''}
                            ${bill.status == 'pending' ? 'bg-yellow-100 text-yellow-700' : ''}
                            ${bill.status == 'cancelled' ? 'bg-red-100 text-red-700' : ''}">
						<i class="fas fa-circle mr-1 text-xs"></i>${bill.status}
					</span>
				</div>

				<!-- Status Update Form -->
				<form action="bill" method="post"
					class="inline-flex items-center gap-2">
					<input type="hidden" name="action" value="update"> <input
						type="hidden" name="billId" value="${bill.billId}"> <label
						for="status" class="text-sm text-gray-600">Update Status:</label>
					<select name="status" id="status"
						class="px-3 py-2 border ${fieldErrors.status != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-cyan-500 focus:border-cyan-500'} rounded-lg transition-colors duration-200">
						<option value="pending"
							${status != null ? (status == 'pending' ? 'selected' : '') : (bill.status=='pending' ? 'selected' : '')}>Pending</option>
						<option value="paid" ${status != null ? (status == 'paid' ? 'selected' : '') : (bill.status=='paid' ? 'selected' : '')}>Paid</option>
						<option value="cancelled"
							${status != null ? (status == 'cancelled' ? 'selected' : '') : (bill.status=='cancelled' ? 'selected' : '')}>
							Cancelled</option>
					</select>
					<button type="submit"
						class="bg-gradient-to-r from-cyan-500 to-blue-600 text-white px-4 py-2 rounded-lg hover:from-cyan-600 hover:to-blue-700 transition-colors duration-200">
						<i class="fas fa-save mr-1"></i>Update
					</button>
				</form>
				<c:if test="${fieldErrors.status != null}">
					<p class="mt-1 text-sm text-red-600">${fieldErrors.status}</p>
				</c:if>
			</div>
		</div>

		<!-- Bill Items Section -->
		<div class="mb-6">
			<h3
				class="text-lg font-semibold text-gray-900 mb-4 flex items-center">
				<i class="fas fa-shopping-cart mr-2 text-cyan-600"></i>Bill Items
			</h3>
			<c:choose>
				<c:when test="${not empty bill.items}">
					<div class="overflow-x-auto border border-gray-200 rounded-lg">
						<table class="min-w-full divide-y divide-gray-200">
							<thead class="bg-cyan-50">
								<tr>
									<th
										class="px-6 py-3 text-left text-xs font-medium text-orange-700 uppercase tracking-wider">
										<i class="fas fa-box mr-1"></i>Product
									</th>
									<th
										class="px-6 py-3 text-left text-xs font-medium text-orange-700 uppercase tracking-wider">
										<i class="fas fa-hashtag mr-1"></i>Quantity
									</th>
									<th
										class="px-6 py-3 text-left text-xs font-medium text-orange-700 uppercase tracking-wider">
										<i class="fas fa-tag mr-1"></i>Unit Price
									</th>

									<th
										class="px-6 py-3 text-left text-xs font-medium text-orange-700 uppercase tracking-wider">
										<i class="fas fa-calculator mr-1"></i>Subtotal
									</th>
								</tr>
							</thead>
							<tbody class="bg-white divide-y divide-gray-200">
								<c:forEach var="item" items="${bill.items}">
									<tr class="hover:bg-gray-50 transition-colors duration-200">
										<td class="px-6 py-4 text-gray-900 font-medium">
											${item.productName}</td>
										<td class="px-6 py-4 text-gray-700">${item.quantity}</td>
										<td class="px-6 py-4 text-gray-700"><fmt:formatNumber
												value="${item.unitPrice}" type="currency"
												currencySymbol="Rs. " /></td>

										<td class="px-6 py-4 text-gray-900 font-semibold"><fmt:formatNumber
												value="${item.subtotal}" type="currency"
												currencySymbol="Rs. " /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:when>
				<c:otherwise>
					<div class="text-center text-gray-600 py-8 bg-gray-50 rounded-lg">
						<i class="fas fa-shopping-cart text-4xl text-gray-400 mb-4"></i>
						<p>No items found for this bill.</p>
					</div>
				</c:otherwise>
			</c:choose>
		</div>

		<!-- Bill Summary -->
		<div class="bg-gray-50 rounded-lg p-6 mb-6">
			<h3 class="text-lg font-semibold text-gray-900 mb-4 flex items-center">
				<i class="fas fa-calculator mr-2 text-cyan-600"></i>Bill Summary
			</h3>
			<div class="grid grid-cols-1 md:grid-cols-2 gap-6">
				<div class="bg-white rounded-lg p-4 border border-gray-200">
					<div class="flex justify-between items-center mb-2">
						<span class="text-sm text-gray-600">Subtotal:</span>
						<span class="text-sm text-gray-900">
							<fmt:formatNumber value="${bill.totalAmount + (bill.discount > 0 ? bill.totalAmount * bill.discount / 100 : 0)}" type="currency" currencySymbol="Rs. " />
						</span>
					</div>
					<c:if test="${bill.discount > 0}">
						<div class="flex justify-between items-center mb-2">
							<span class="text-sm text-gray-600">Discount (${bill.discount}%):</span>
							<span class="text-sm text-green-600">
								- <fmt:formatNumber value="${bill.totalAmount * bill.discount / 100}" type="currency" currencySymbol="Rs. " />
							</span>
						</div>
					</c:if>
					<hr class="my-2">
					<div class="flex justify-between items-center">
						<span class="text-lg font-semibold text-gray-900">Total Amount:</span>
						<span class="text-xl font-bold text-cyan-600">
							<fmt:formatNumber value="${bill.totalAmount}" type="currency" currencySymbol="Rs. " />
						</span>
					</div>
				</div>
			</div>
		</div>

		<!-- Action Buttons -->
		<div class="flex items-center gap-4 mt-8 justify-center">
			<button onclick="printBill()"
				class="bg-green-600 text-white px-6 py-3 rounded-lg hover:bg-green-700 transition-colors duration-200 flex items-center">
				<i class="fas fa-print mr-2"></i>Print Bill
			</button>
			<button onclick="showEmailModal()"
				class="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors duration-200 flex items-center">
				<i class="fas fa-envelope mr-2"></i>Send Email
			</button>
			<a href="bill?action=list"
				class="bg-gray-100 text-gray-700 px-6 py-3 rounded-lg hover:bg-gray-200 transition-colors duration-200 flex items-center">
				<i class="fas fa-arrow-left mr-2"></i>Back
			</a>
		</div>
	</div>
</div>

<!-- Print Styles -->
<style>
@media print {
	body * {
		visibility: hidden;
	}
	.print-content, .print-content * {
		visibility: visible;
	}
	.print-content {
		position: absolute;
		left: 0;
		top: 0;
		width: 100%;
	}
	.navbar, .sidebar, .print-btn, .back-btn {
		display: none !important;
	}
}
</style>

<!-- Print Content Template -->
<div id="printContent" class="print-content" style="display: none;">
	<div style="font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px;">
		<!-- Header -->
		<div style="text-align: center; margin-bottom: 30px; padding: 20px; border-bottom: 3px solid #ea580c; background: #fff7ed;">
			<div style="font-size: 32px; font-weight: bold; color: #ea580c; margin-bottom: 8px;">Ocean View Resort</div>
			<div style="font-size: 16px; color: #c2410c; margin-bottom: 12px; font-style: italic;">Your Trusted Destination for a Perfect Getaway</div>
			<div style="font-size: 13px; color: #7c2d12; line-height: 1.8;">
				<strong>Address:</strong> Colombo City, Sri Lanka<br>
				<strong>Phone:</strong> +94 11 2345678 | <strong>Email:</strong> info@oceanviewresort.com<br>
				<strong>Website:</strong> www.oceanviewresort.com
			</div>
		</div>

		<!-- Bill Header -->
		<div style="display: flex; justify-content: space-between; margin-bottom: 30px; padding: 25px; background: #fff7ed; border-radius: 8px; border: 1px solid #fdba74;">
			<div style="flex: 1;">
				<h3 style="color: #ea580c; margin-bottom: 18px; font-size: 20px; border-bottom: 2px solid #fdba74; padding-bottom: 8px;">Bill To:</h3>
				<p style="margin-bottom: 8px; font-size: 14px; color: #7c2d12;"><strong>Name:</strong> ${bill.customerName}</p>
				<p style="margin-bottom: 8px; font-size: 14px; color: #7c2d12;"><strong>Account Number:</strong> ${bill.accountNumber}</p>
				<p style="margin-bottom: 8px; font-size: 14px; color: #7c2d12;"><strong>Date:</strong> ${bill.billDate}</p>
			</div>
			<div style="flex: 1;">
				<h3 style="color: #ea580c; margin-bottom: 18px; font-size: 20px; border-bottom: 2px solid #fdba74; padding-bottom: 8px;">Bill Information:</h3>
				<div style="font-size: 18px; font-weight: bold; color: #ea580c; margin-bottom: 5px;">Bill #${bill.billId}</div>
				<div style="color: #7c2d12; font-size: 14px;">Generated: ${bill.billDate}</div>
				<p style="margin-bottom: 8px; font-size: 14px; color: #7c2d12;"><strong>Status:</strong> 
					<c:choose>
						<c:when test="${bill.status == 'paid'}">
							<span style="padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; background: #d1fae5; color: #065f46; border: 1px solid #10b981; display: inline-block; text-transform: uppercase; letter-spacing: 0.5px;">${bill.status}</span>
						</c:when>
						<c:when test="${bill.status == 'cancelled'}">
							<span style="padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; background: #fee2e2; color: #991b1b; border: 1px solid #ef4444; display: inline-block; text-transform: uppercase; letter-spacing: 0.5px;">${bill.status}</span>
						</c:when>
						<c:otherwise>
							<span style="padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; background: #fef3c7; color: #92400e; border: 1px solid #f59e0b; display: inline-block; text-transform: uppercase; letter-spacing: 0.5px;">${bill.status}</span>
						</c:otherwise>
					</c:choose>
				</p>
			</div>
		</div>

		<!-- Items Table -->
		<table style="width: 100%; border-collapse: collapse; margin-bottom: 30px; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);">
			<thead>
				<tr style="background: #ea580c; color: white;">
					<th style="padding: 15px; text-align: left; font-weight: 600; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px;">#</th>
					<th style="padding: 15px; text-align: left; font-weight: 600; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px;">Product</th>
					<th style="padding: 15px; text-align: left; font-weight: 600; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px;">Quantity</th>
					<th style="padding: 15px; text-align: left; font-weight: 600; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px;">Unit Price</th>
					<th style="padding: 15px; text-align: left; font-weight: 600; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px;">Subtotal</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${bill.items}" varStatus="status">
					<tr style="background: white;">
						<td style="padding: 15px; text-align: left; color: #7c2d12; font-size: 14px;"><strong>${status.index + 1}</strong></td>
						<td style="padding: 15px; text-align: left; color: #7c2d12; font-size: 14px;"><strong>${item.productName}</strong></td>
						<td style="padding: 15px; text-align: left; color: #7c2d12; font-size: 14px;">${item.quantity}</td>
						<td style="padding: 15px; text-align: left; color: #7c2d12; font-size: 14px;"><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="Rs. " /></td>
						<td style="padding: 15px; text-align: left; color: #7c2d12; font-size: 14px;"><strong><fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="Rs. " /></strong></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<!-- Bill Summary -->
		<div style="margin: 30px 0; padding: 25px; background: #fff7ed; border-radius: 8px; border: 1px solid #fdba74;">
			<h3 style="color: #ea580c; margin-bottom: 20px; font-size: 18px; border-bottom: 2px solid #fdba74; padding-bottom: 8px;">Bill Summary</h3>
			<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
				<span style="color: #7c2d12; font-size: 14px;">Subtotal:</span>
				<span style="color: #7c2d12; font-size: 14px; font-weight: bold;">
					<fmt:formatNumber value="${bill.totalAmount + (bill.discount > 0 ? bill.totalAmount * bill.discount / 100 : 0)}" type="currency" currencySymbol="Rs. " />
				</span>
			</div>
			<c:if test="${bill.discount > 0}">
				<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
					<span style="color: #7c2d12; font-size: 14px;">Discount (${bill.discount}%):</span>
					<span style="color: #059669; font-size: 14px; font-weight: bold;">
						- <fmt:formatNumber value="${bill.totalAmount * bill.discount / 100}" type="currency" currencySymbol="Rs. " />
					</span>
				</div>
			</c:if>
			<hr style="margin: 15px 0; border: none; border-top: 1px solid #fdba74;">
			<div style="display: flex; justify-content: space-between; align-items: center;">
				<span style="color: #ea580c; font-size: 18px; font-weight: bold;">Total Amount:</span>
				<span style="color: #ea580c; font-size: 24px; font-weight: bold;">
					<fmt:formatNumber value="${bill.totalAmount}" type="currency" currencySymbol="Rs. " />
				</span>
			</div>
		</div>

		<!-- Footer -->
		<div style="margin-top: 50px; padding: 25px; border-top: 2px solid #fdba74; text-align: center; font-size: 13px; color: #7c2d12; background: #fff7ed; border-radius: 8px;">
			<p><strong>Thank you for your business!</strong></p>
			<p>For any queries, please contact us at info@oceanviewresort.com or call +94 11 2345678</p>
			<p>This is a computer generated bill. No signature required.</p>
			<p style="margin-top: 15px; font-size: 11px; color: #9a3412;">
				<strong>Ocean View Resort</strong> - Your Trusted Destination for a Perfect Getaway
			</p>
		</div>
	</div>
</div>

<!-- Email Modal -->
<div id="emailModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full hidden z-50">
	<div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
		<div class="mt-3">
			<div class="flex items-center justify-between mb-4">
				<h3 class="text-lg font-semibold text-gray-900 flex items-center">
					<i class="fas fa-envelope mr-2 text-blue-600"></i>Send Bill via Email
				</h3>
				<button onclick="hideEmailModal()" class="text-gray-400 hover:text-gray-600">
					<i class="fas fa-times"></i>
				</button>
			</div>
			
			<form id="emailForm" action="bill" method="get" class="space-y-4">
				<input type="hidden" name="action" value="email">
				<input type="hidden" name="id" value="${bill.billId}">
				
				<div>
					<label for="email" class="block text-sm font-medium text-gray-700 mb-2">
						<i class="fas fa-envelope mr-1"></i>Email Address
					</label>
					<input type="email" id="email" name="email" required
						class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200"
						placeholder="Enter recipient email address">
				</div>
				
				<div class="bg-blue-50 border border-blue-200 rounded-lg p-3">
					<p class="text-sm text-blue-800">
						<i class="fas fa-info-circle mr-1"></i>
						This will send a professional HTML bill to the specified email address.
					</p>
				</div>
				
				<div class="flex items-center justify-end space-x-3 pt-4">
					<button type="button" onclick="hideEmailModal()"
						class="px-4 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors duration-200">
						Cancel
					</button>
					<button type="submit"
						class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-200 flex items-center">
						<i class="fas fa-paper-plane mr-2"></i>Send Email
					</button>
				</div>
			</form>
		</div>
	</div>
</div>

<script>
function printBill() {
	// Show the print content
	document.getElementById('printContent').style.display = 'block';
	
	// Trigger print
	window.print();
	
	// Hide the print content after printing
	setTimeout(function() {
		document.getElementById('printContent').style.display = 'none';
	}, 1000);
}

function showEmailModal() {
	document.getElementById('emailModal').classList.remove('hidden');
	document.getElementById('email').focus();
}

function hideEmailModal() {
	document.getElementById('emailModal').classList.add('hidden');
	document.getElementById('emailForm').reset();
}

// Close modal when clicking outside
document.getElementById('emailModal').addEventListener('click', function(e) {
	if (e.target === this) {
		hideEmailModal();
	}
});

// Handle form submission
document.getElementById('emailForm').addEventListener('submit', function(e) {
	const email = document.getElementById('email').value.trim();
	if (!email) {
		e.preventDefault();
		alert('Please enter an email address');
		return;
	}
	
	// Basic email validation
	const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	if (!emailRegex.test(email)) {
		e.preventDefault();
		alert('Please enter a valid email address');
		return;
	}
	
	// Show loading state
	const submitBtn = this.querySelector('button[type="submit"]');
	const originalText = submitBtn.innerHTML;
	submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Sending...';
	submitBtn.disabled = true;
	
	// Reset button after a delay (in case of error)
	setTimeout(function() {
		submitBtn.innerHTML = originalText;
		submitBtn.disabled = false;
	}, 5000);
});
</script>
	</div>
</div>
<footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
    <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
</footer>
    </div>
</div>
</body>
</html>