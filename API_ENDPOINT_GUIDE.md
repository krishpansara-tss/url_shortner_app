# URL Shortener API - Complete Endpoint Documentation

## 📊 IMPLEMENTATION STATUS SUMMARY

| Category | Total | ✅ Done | ⚠️ Partial | ❌ Missing |
|----------|-------|---------|-----------|-----------|
| **Users** | 9 | 9 | 0 | 0 |
| **URLs** | 10 | 6 | 0 | 4 |
| **Payments** | 6 | 6 | 0 | 0 |
| **Purchases** | 3 | 0 | 0 | 3 |
| **Media** | 7 | 7 | 0 | 0 |
| **Auth** | 8 | 8 | 0 | 0 |
| **Admin** | 23 | 0 | 1 | 22 |
| **Scheduled Tasks** | 3 | 0 | 0 | 3 |
| **TOTAL** | 69 | 44 | 1 | 24 |

---

## 🔐 USER MANAGEMENT ENDPOINTS (`/api/v1/users`) ✅ COMPLETE

### Create User
```
POST /api/v1/users
Status: ✅ IMPLEMENTED

Request:
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "password123"
}

Response: 201 CREATED
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "isEmailVerified": false,
  "createdAt": "2026-08-12T10:00:00"
}
```

### Get All Users (Admin)
```
GET /api/v1/users
Status: ✅ IMPLEMENTED

Response: 200 OK - List of users
```

### Get User by ID
```
GET /api/v1/users/{id}
Status: ✅ IMPLEMENTED

Response: 200 OK - User details
```

### Update User
```
PUT /api/v1/users/{id}
Status: ✅ IMPLEMENTED

Request: { firstName, lastName, ... }
Response: 200 OK - Updated user
```

### Delete User
```
DELETE /api/v1/users/{id}
Status: ✅ IMPLEMENTED

Response: 204 NO CONTENT
```

### Get Current User Profile
```
GET /api/v1/users/me
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Response: 200 OK - Current user details
```

### Update Current User Profile
```
PUT /api/v1/users/me
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Request: { firstName, lastName, ... }
Response: 200 OK - Updated profile
```

### Change Password
```
PUT /api/v1/users/me/password
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Request: { oldPassword, newPassword, confirmPassword }
Response: 200 OK
```

### Get User Usage Stats
```
GET /api/v1/users/me/usage
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Response: 200 OK
{
  "totalUrls": 42,
  "totalClicks": 1234,
  "storageUsed": "2.5 MB",
  "mediaCount": 15,
  "currentPlan": "PRO"
}
```

---

## 🔗 URL MANAGEMENT ENDPOINTS (`/api/v1/urls`)

### Create Short URL
```
POST /api/v1/urls
Status: ✅ IMPLEMENTED

Request:
{
  "longUrl": "https://example.com/very-long-url",
  "expiryDate": "2026-12-31",
  "maxClicks": 1000
}
Query: userId={userId}

Response: 201 CREATED
{
  "id": 1,
  "shortCode": "abc123",
  "shortUrl": "https://short.link/abc123",
  "longUrl": "https://example.com/very-long-url",
  "createdAt": "2026-08-12"
}
```

### Create Custom URL
```
POST /api/v1/urls/custom
Status: ✅ IMPLEMENTED

Request:
{
  "longUrl": "https://example.com/url",
  "customAlias": "mylink",
  "expiryDate": "2026-12-31"
}
Query: userId={userId}

Response: 201 CREATED
{
  "id": 1,
  "shortCode": "mylink",
  "shortUrl": "https://short.link/mylink",
  "longUrl": "https://example.com/url"
}
```

### Get All URLs (User's URLs)
```
GET /api/v1/urls
Status: ✅ IMPLEMENTED

Query: userId={userId}&page=0&size=10
Response: 200 OK
{
  "content": [ { url objects } ],
  "totalElements": 42,
  "totalPages": 5,
  "currentPage": 0
}
```

### Get URL by ID
```
GET /api/v1/urls/{id}
Status: ✅ IMPLEMENTED

Response: 200 OK - URL details
```

### Update URL
```
PUT /api/v1/urls/{id}
Status: ✅ IMPLEMENTED

Query: userId={userId}
Request: { expiryDate, maxClicks, ... }
Response: 200 OK - Updated URL
```

### Delete URL
```
DELETE /api/v1/urls/{id}
Status: ✅ IMPLEMENTED

Query: userId={userId}
Response: 204 NO CONTENT
```

### Change URL Status (Active/Inactive)
```
PATCH /api/v1/urls/{id}/status
Status: ❌ MISSING

Query: userId={userId}
Request: { status: "ACTIVE" | "INACTIVE" }
Response: 200 OK - Updated status
```

### Get URL Statistics
```
GET /api/v1/urls/{id}/stats
Status: ❌ MISSING

Query: userId={userId}
Response: 200 OK
{
  "totalClicks": 500,
  "uniqueVisitors": 350,
  "clicks": [ { date, count } ],
  "topCountries": [ { country, count } ],
  "topBrowsers": [ { browser, count } ]
}
```

### Renew Expiring URL
```
POST /api/v1/urls/{id}/renew
Status: ❌ MISSING

Query: userId={userId}
Request: { newExpiryDate: "2027-12-31" }
Response: 200 OK - Extended URL
```

### Get URL Purchases
```
GET /api/v1/urls/{id}/purchases
Status: ❌ MISSING (Commented out)

Query: userId={userId}
Response: 200 OK - List of purchases for this URL
```

---

## 💳 PAYMENT ENDPOINTS (`/api/v1/payments`) ✅ COMPLETE

### Initiate Payment
```
POST /api/v1/payments
Status: ✅ IMPLEMENTED

Request:
{
  "userId": 1,
  "urlId": 5,
  "paymentType": "URL_PREMIUM",
  "amount": 9.99
}

Response: 201 CREATED
{
  "id": 1,
  "status": "PENDING",
  "amount": 9.99,
  "createdAt": "2026-08-12T10:00:00"
}
```

### Get All Payments (User)
```
GET /api/v1/payments
Status: ✅ IMPLEMENTED

Query: userId={userId}&page=0&size=10
Response: 200 OK - Paginated payments
```

### Get Payment by ID
```
GET /api/v1/payments/{id}
Status: ✅ IMPLEMENTED

Query: userId={userId}
Response: 200 OK - Payment details
```

### Process Payment
```
POST /api/v1/payments/{id}/process
Status: ✅ IMPLEMENTED

Response: 200 OK
{
  "id": 1,
  "status": "COMPLETED",
  "transactionId": "TXN-123-ABC"
}
```

### Cancel Payment
```
POST /api/v1/payments/{id}/cancel
Status: ✅ IMPLEMENTED

Query: userId={userId}
Response: 200 OK - Cancelled payment
```

### Get Payment Receipt
```
GET /api/v1/payments/{id}/receipt
Status: ✅ IMPLEMENTED

Query: userId={userId}
Response: 200 OK - Receipt details
```

---

## 🛒 PURCHASE ENDPOINTS (`/api/v1/purchases`) ❌ NOT IMPLEMENTED

### Get All Purchases
```
GET /api/v1/purchases
Status: ❌ MISSING

Query: page=0&size=10
Response: 200 OK - Paginated purchases
```

### Get Purchase by ID
```
GET /api/v1/purchases/{id}
Status: ❌ MISSING

Response: 200 OK - Purchase details
```

### Filter Purchases by Type
```
GET /api/v1/purchases?type={type}
Status: ❌ MISSING

Query: type=PREMIUM&page=0&size=10
Response: 200 OK - Filtered purchases
```

**Need to Create:**
- `PurchaseController.java`
- `PurchaseServiceImpl.java`
- `PurchaseResponseDto.java`

---

## 📸 MEDIA ENDPOINTS - PROFILE PICTURES & URL MEDIA ✅ IMPLEMENTED

### Upload Profile Picture
```
POST /api/v1/users/me/profile-picture
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Request: form-data with "image" file
Response: 201 CREATED
{
  "fileName": "profile.jpg",
  "mediaType": "IMAGE",
  "fileUrl": "https://res.cloudinary.com/.../profile.jpg",
  "createdAt": "2026-08-12T10:00:00"
}
```

### Get Profile Picture
```
GET /api/v1/users/{id}/profile-picture
Status: ✅ IMPLEMENTED

Response: 200 OK - Profile picture details
```

### Delete Profile Picture
```
DELETE /api/v1/users/me/profile-picture
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Response: 204 NO CONTENT
```

### Upload Media to URL
```
POST /api/v1/urls/{urlId}/media
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Request: form-data with "file" 
Response: 201 CREATED
{
  "mediaId": 1,
  "fileName": "document.pdf",
  "mediaType": "DOCUMENT",
  "fileUrl": "https://res.cloudinary.com/.../document.pdf",
  "createdAt": "2026-08-12T10:00:00"
}
```

### Get All Media for URL
```
GET /api/v1/urls/{urlId}/media
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Response: 200 OK - List of media
```

### Get Media Details by ID
```
GET /api/v1/urls/{urlId}/media/{mediaId}
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Response: 200 OK - Media metadata
```

### Delete Media
```
DELETE /api/v1/urls/{urlId}/media/{mediaId}
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Response: 204 NO CONTENT
```

---

## 🔑 AUTH ENDPOINTS (`/api/v1/auth`) ✅ COMPLETE

### Register
```
POST /api/v1/auth/register
Status: ✅ IMPLEMENTED

Request: { firstName, lastName, email, password }
Response: 201 CREATED - User with token
```

### Verify Email
```
POST /api/v1/auth/verify-email
Status: ✅ IMPLEMENTED

Request: { email, otpCode: "123456" }
Response: 200 OK - Email verified
```

### Login
```
POST /api/v1/auth/login
Status: ✅ IMPLEMENTED

Request: { email, password }
Response: 200 OK
{
  "accessToken": "jwt-token",
  "refreshToken": "refresh-token",
  "user": { ... }
}
```

### Get Me
```
GET /api/v1/auth/me
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Response: 200 OK - Current user
```

### Logout
```
POST /api/v1/auth/logout
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}, Authorization: Bearer {token}
Response: 200 OK - Logged out
```

### Logout All Devices
```
POST /api/v1/auth/logout-all
Status: ✅ IMPLEMENTED

Header: X-User-Id: {userId}
Response: 200 OK - Logged out from all devices
```

### Forgot Password
```
POST /api/v1/auth/forgot-password
Status: ✅ IMPLEMENTED

Request: { email }
Response: 200 OK - OTP sent to email
```

### Reset Password
```
POST /api/v1/auth/reset-password
Status: ✅ IMPLEMENTED

Request: { email, otpCode, newPassword }
Response: 200 OK - Password reset
```

---

## 👨‍💼 ADMIN ENDPOINTS (`/api/v1/admin/`) ❌ MOSTLY MISSING

### Admin User Management

#### List All Users
```
GET /api/v1/admin/users
Status: ❌ MISSING

Query: page=0&size=10
Response: 200 OK - All users
```

#### Get User Details
```
GET /api/v1/admin/users/{id}
Status: ❌ MISSING

Response: 200 OK - User details
```

#### Block User
```
PATCH /api/v1/admin/users/{id}/block
Status: ❌ MISSING

Request: { reason: "Spam activity" }
Response: 200 OK - User blocked
```

#### Activate User
```
PATCH /api/v1/admin/users/{id}/activate
Status: ❌ MISSING

Response: 200 OK - User activated
```

#### Delete User (Admin Override)
```
DELETE /api/v1/admin/users/{id}
Status: ❌ MISSING

Response: 204 NO CONTENT
```

### Admin Payment Management

#### List All Payments
```
GET /api/v1/admin/payments
Status: ❌ MISSING

Query: page=0&size=10
Response: 200 OK - All payments
```

#### Get Payment Details
```
GET /api/v1/admin/payments/{id}
Status: ❌ MISSING

Response: 200 OK - Payment details
```

#### Filter Payments by Status
```
GET /api/v1/admin/payments?status={status}
Status: ❌ MISSING

Query: status=COMPLETED&page=0&size=10
Response: 200 OK - Filtered payments
```

#### Payment Statistics
```
GET /api/v1/admin/payments/stats
Status: ❌ MISSING

Response: 200 OK
{
  "totalRevenue": 50000,
  "totalTransactions": 1250,
  "averageTransactionValue": 40,
  "statusBreakdown": { "COMPLETED": 1200, "PENDING": 50 }
}
```

### Admin Purchase Management

#### List All Purchases
```
GET /api/v1/admin/purchases
Status: ❌ MISSING

Query: page=0&size=10
Response: 200 OK - All purchases
```

#### Get Purchase Details
```
GET /api/v1/admin/purchases/{id}
Status: ❌ MISSING

Response: 200 OK - Purchase details
```

#### Filter Purchases by Type
```
GET /api/v1/admin/purchases?type={type}
Status: ❌ MISSING

Query: type=PREMIUM&page=0&size=10
Response: 200 OK - Filtered purchases
```

#### Purchase Statistics
```
GET /api/v1/admin/purchases/stats
Status: ❌ MISSING

Response: 200 OK
{
  "totalPurchases": 2500,
  "totalSpent": 75000,
  "typeBreakdown": { "PREMIUM": 1500, "BASIC": 1000 }
}
```

### System Configuration (⚠️ Partial)

#### Get Configuration
```
GET /api/v1/admin/config
Status: ⚠️ PARTIAL (exists as /api/v1/admin/system-config)

Response: 200 OK - System config
```

#### Get Config by Key
```
GET /api/v1/admin/config/{key}
Status: ❌ MISSING

Response: 200 OK - Config value
```

#### Create Config
```
POST /api/v1/admin/config
Status: ❌ MISSING

Request: { key: "MAX_URLS_PER_USER", value: "1000" }
Response: 201 CREATED
```

#### Update Config
```
PUT /api/v1/admin/config/{key}
Status: ❌ MISSING

Request: { value: "2000" }
Response: 200 OK - Updated config
```

#### Delete Config
```
DELETE /api/v1/admin/config/{key}
Status: ❌ MISSING

Response: 204 NO CONTENT
```

### Rate Limiting Management

#### Create Rate Limit
```
POST /api/v1/admin/rate-limits
Status: ❌ MISSING

Request: { name: "URLs per hour", limit: 100, windowSeconds: 3600 }
Response: 201 CREATED
```

#### List Rate Limits
```
GET /api/v1/admin/rate-limits
Status: ❌ MISSING

Response: 200 OK - All rate limits
```

#### Get Rate Limit by ID
```
GET /api/v1/admin/rate-limits/{rateLimitId}
Status: ❌ MISSING

Response: 200 OK - Rate limit details
```

#### Update Rate Limit
```
PUT /api/v1/admin/rate-limits/{rateLimitId}
Status: ❌ MISSING

Request: { limit: 150 }
Response: 200 OK - Updated rate limit
```

#### Toggle Rate Limit Status
```
PATCH /api/v1/admin/rate-limits/{rateLimitId}/status
Status: ❌ MISSING

Request: { active: true }
Response: 200 OK - Status updated
```

#### Delete Rate Limit
```
DELETE /api/v1/admin/rate-limits/{rateLimitId}
Status: ❌ MISSING

Response: 204 NO CONTENT
```

---

## ⏰ SCHEDULED TASKS ❌ NOT IMPLEMENTED

### 1. URL Expiry Scheduler
```
Status: ❌ MISSING

Description: Runs daily at 2:00 AM
- Check all URLs with expiry date in the past
- Set status to EXPIRED
- Disable redirect functionality
- Notify users of expiring URLs (24 hours before)
```

### 2. Revoked Token Cleanup Scheduler
```
Status: ❌ MISSING

Description: Runs daily at 3:00 AM
- Remove revoked tokens older than 30 days
- Clean up old session records
```

### 3. Pending Payment Cleanup Scheduler
```
Status: ❌ MISSING

Description: Runs daily at 4:00 AM
- Find payments in PENDING status for > 24 hours
- Auto-cancel old pending payments
- Notify users
```

---

## 🔄 COMPLETE DATA FLOW DIAGRAM

```
USER REGISTRATION FLOW:
═════════════════════════════════════════════════════════════════

POST /api/v1/auth/register
         ↓
   AuthController
         ↓
   AuthServiceImpl
         ↓
   UserRepository (save new user)
         ↓
   Send verification email with OTP
         ↓
POST /api/v1/auth/verify-email
         ↓
   Verify OTP code
         ↓
   Mark email as verified
         ↓
✅ User account activated


URL CREATION & ACCESS FLOW:
═════════════════════════════════════════════════════════════════

POST /api/v1/urls
         ↓
   UrlController
         ↓
   UrlServiceImp
         ↓
   1. Generate unique short code
   2. Save to UrlRepository
   3. Create UrlRedirect mapping
         ↓
   Return: shortUrl, longUrl, shortCode
         ↓
GET http://short.link/abc123 (redirect endpoint)
         ↓
   UrlController.redirect()
         ↓
   UrlServiceImp.resolveShortUrlAndRecordVisit()
         ↓
   1. Find URL in cache/DB
   2. Record click + analytics
   3. Check expiry & max clicks
         ↓
   302 Found: Location: {longUrl}
         ↓
✅ User redirected to original URL


PAYMENT & PURCHASE FLOW:
═════════════════════════════════════════════════════════════════

POST /api/v1/payments (initiate)
         ↓
   PaymentController
         ↓
   PaymentServiceImpl
         ↓
   1. Create Payment record (PENDING)
   2. Generate payment link
   3. Send to payment gateway
         ↓
   Return: paymentId, status, gateway_link
         ↓
(User processes payment in gateway)
         ↓
POST /api/v1/payments/{id}/process (callback)
         ↓
   Update Payment status to COMPLETED
         ↓
   Create Purchase record
         ↓
   Update URL features (premium, extended expiry, etc.)
         ↓
   Send confirmation email
         ↓
✅ Purchase completed


URL ANALYTICS FLOW:
═════════════════════════════════════════════════════════════════

Every click on short URL:
         ↓
   Record in UrlVisit/Analytics table:
   - timestamp
   - country (geo-IP)
   - browser/device
   - referrer
         ↓
GET /api/v1/urls/{id}/stats
         ↓
   UrlServiceImp.getUrlStats()
         ↓
   Query UrlVisit table
   Aggregate data by:
   - date
   - country
   - browser
   - time range
         ↓
✅ Return statistics dashboard data


MEDIA GENERATION FLOW:
═════════════════════════════════════════════════════════════════

POST /api/v1/urls/{urlId}/media/qr
         ↓
   MediaController (new - to be created)
         ↓
   MediaServiceImpl (new)
         ↓
   Generate QR code with short URL
         ↓
   Upload to Cloudinary via ImageUploadService
         ↓
   Save Media record:
   - type: QR_CODE
   - url: cloudinary_link
   - urlId: foreign key
         ↓
   Return: mediaId, cloudinaryUrl
         ↓
✅ QR code generated & stored


ADMIN ANALYTICS FLOW:
═════════════════════════════════════════════════════════════════

GET /api/v1/admin/payments/stats
         ↓
   AdminController (new)
         ↓
   AdminServiceImpl (new)
         ↓
   @PreAuthorize("hasRole('ADMIN')")
         ↓
   Query Payment table with aggregations:
   - SUM(amount)
   - COUNT(*) by status
   - AVG(amount)
   - DATE_FORMAT grouping
         ↓
✅ Return aggregated analytics
```

---

## 📋 IMPLEMENTATION CHECKLIST

### ✅ COMPLETED (44/69)
- [x] User Management (9/9)
- [x] Auth Endpoints (8/8)
- [x] Payment Endpoints (6/6)
- [x] Basic URL Management (6/10)
- [x] Image Upload (1/1 - Cloudinary setup done)
- [x] Media/Profile Pictures (7/7)

### ⚠️ IN PROGRESS (1/69)
- [ ] System Config (1/5 - exists, needs completion)

### ❌ TODO (24/69)

**HIGH PRIORITY:**
- [ ] Complete URL endpoints (stats, status, renew, purchases)
- [ ] Purchase Management (3 endpoints + Controller + Service)

**MEDIUM PRIORITY:**
- [ ] Admin User Management (5 endpoints)
- [ ] Admin Payment Management (4 endpoints)
- [ ] Admin Purchase Management (4 endpoints)

**LOW PRIORITY:**
- [ ] Rate Limiting Management (6 endpoints)
- [ ] Scheduled Tasks (3 background jobs)

---

## 🛠️ NEXT STEPS

1. **Complete URL endpoints** (missing: stats, status, renew)
2. **Create PurchaseController & Service**
3. **Create MediaController & Service** (QR/Thumbnail with Cloudinary)
4. **Create AdminController & Service**
5. **Implement Scheduled Tasks**
6. **Add comprehensive error handling** across all endpoints
7. **Create Postman Collection** with all endpoints

---

## 📝 NOTES

- All endpoints use exception handling pattern with `GlobalExceptionHandler`
- User authentication via `X-User-Id` header
- Admin endpoints require `@PreAuthorize("hasRole('ADMIN')")`
- Pagination using `PageResponse<T>` DTO
- Cloudinary integration ready for image upload, QR, thumbnail
- Standard HTTP status codes: 200 OK, 201 CREATED, 204 NO CONTENT, 404 NOT FOUND, 400 BAD REQUEST
