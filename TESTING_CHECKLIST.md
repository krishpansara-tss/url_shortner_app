# URL SHORTENER API - COMPLETE TESTING CHECKLIST

## ✅ ALREADY TESTED (From Your Screenshot)

### Users Endpoints (9/9) ✅
- [x] POST /api/v1/users - Create users
- [x] GET /api/v1/users - List all users
- [x] GET /api/v1/users/{id} - Found user via id
- [x] PUT /api/v1/users/{id} - Updated name, email, password
- [x] DELETE /api/v1/users/{id} - Delete user
- [x] GET /api/v1/users/me - Get login user
- [x] GET /api/v1/users/me/usage - Login user usage
- [x] PUT /api/v1/users/me/info - Login user info change
- [x] PUT /api/v1/users/me/password - Login change password

### Auth/OTP Endpoints (8/8) ✅
- [x] POST /api/v1/auth/register - (via Users POST)
- [x] POST /api/v1/auth/verify-email - (OTP verify)
- [x] POST /api/v1/auth/resend - (OTP resend)
- [x] POST /api/v1/auth/login - (via get login user)
- [x] POST /api/v1/auth/logout
- [x] POST /api/v1/auth/logout-all
- [x] POST /api/v1/auth/forgot-password
- [x] POST /api/v1/auth/reset-password

---

## 🔄 NEED RE-TESTING (Due to Media Implementation)

### User Profile Picture - NEW (3 endpoints)
```
POST /api/v1/users/me/profile-picture
- Header: X-User-Id: 1
- Body: form-data with image file
- Expected: 201 CREATED with fileUrl

GET /api/v1/users/{id}/profile-picture
- Path: /api/v1/users/1/profile-picture
- Expected: 200 OK with profilePictureUrl

DELETE /api/v1/users/me/profile-picture
- Header: X-User-Id: 1
- Expected: 204 NO CONTENT
```

**Why Re-test:** Users table now has profilePicturePath field. Need to verify:
- Profile pic uploads correctly
- File appears in Cloudinary
- User object includes profile picture when fetched

---

## ⏳ PENDING - STILL TODO (24/69 endpoints)

### URL Endpoints (4 missing - HIGH PRIORITY)
```
PATCH /api/v1/urls/{id}/status
- Update URL status (ACTIVE/INACTIVE)
- Status: ❌ NOT YET TESTED

GET /api/v1/urls/{id}/stats
- Get URL analytics (clicks, visitors, etc.)
- Status: ❌ NOT YET TESTED

POST /api/v1/urls/{id}/renew
- Extend expiry date of URL
- Status: ❌ NOT YET TESTED

GET /api/v1/urls/{id}/purchases
- List purchases related to URL
- Status: ❌ NOT YET TESTED
```

### URL Media Endpoints (4 endpoints - NEW)
```
POST /api/v1/urls/{urlId}/media
- Upload file to URL
- Status: ❌ NOT YET TESTED

GET /api/v1/urls/{urlId}/media
- List all media for URL
- Status: ❌ NOT YET TESTED

GET /api/v1/urls/{urlId}/media/{mediaId}
- Get media details
- Status: ❌ NOT YET TESTED

DELETE /api/v1/urls/{urlId}/media/{mediaId}
- Delete media file
- Status: ❌ NOT YET TESTED
```

### Payment Endpoints (6/6 - Already should be working)
```
✅ POST /api/v1/payments
✅ GET /api/v1/payments
✅ GET /api/v1/payments/{id}
✅ POST /api/v1/payments/{id}/process
✅ POST /api/v1/payments/{id}/cancel
✅ GET /api/v1/payments/{id}/receipt
```
**Status:** Need to verify still working

### Purchase Endpoints (3 missing)
```
GET /api/v1/purchases
- List all purchases
- Status: ❌ NOT YET TESTED

GET /api/v1/purchases/{id}
- Get purchase details
- Status: ❌ NOT YET TESTED

GET /api/v1/purchases?type={type}
- Filter purchases by type
- Status: ❌ NOT YET TESTED
```

### Admin Endpoints (23 missing - FUTURE WORK)
```
User Management:
  GET /api/v1/admin/users
  GET /api/v1/admin/users/{id}
  PATCH /api/v1/admin/users/{id}/block
  PATCH /api/v1/admin/users/{id}/activate
  DELETE /api/v1/admin/users/{id}

Payment Management:
  GET /api/v1/admin/payments
  GET /api/v1/admin/payments/{id}
  GET /api/v1/admin/payments?status={status}
  GET /api/v1/admin/payments/stats

Purchase Management:
  GET /api/v1/admin/purchases
  GET /api/v1/admin/purchases/{id}
  GET /api/v1/admin/purchases?type={type}
  GET /api/v1/admin/purchases/stats

Config & Rate Limiting:
  GET/POST/PUT/DELETE /api/v1/admin/config/...
  POST/GET/PUT/PATCH/DELETE /api/v1/admin/rate-limits/...
```

### Scheduled Tasks (3 - Not testable via API)
```
URL Expiry Scheduler
Revoked Token Cleanup Scheduler
Pending Payment Cleanup Scheduler
```

---

## 📊 TESTING PRIORITY

### 🔴 URGENT (Test NOW before moving further)
1. ✅ User Profile Picture endpoints (3) - NEW!
2. ✅ URL Media endpoints (4) - NEW!
3. ⚠️ Re-verify existing Payments (6) - Ensure still work

### 🟡 HIGH (Next phase)
1. URL endpoints: stats, status, renew, purchases (4)
2. Purchase endpoints (3)

### 🟢 MEDIUM (Later)
1. Admin endpoints (23)
2. Scheduled tasks (3)

---

## 🧪 RECOMMENDED TESTING FLOW

### Phase 1: Verify Profile Pictures (3 tests)
```
1. POST /api/v1/users/me/profile-picture
   - Upload profile.jpg
   - Verify returns fileUrl from Cloudinary
   - Check Cloudinary dashboard

2. GET /api/v1/users/1/profile-picture
   - Should return uploaded profile picture URL
   - Verify fileUrl matches

3. DELETE /api/v1/users/me/profile-picture
   - Should return 204 NO CONTENT
   - Verify file deleted from Cloudinary
```

### Phase 2: Verify URL Media (4 tests)
```
1. POST /api/v1/urls/1/media
   - Upload document.pdf to URL
   - Verify mediaId, fileName, mediaType, fileUrl

2. GET /api/v1/urls/1/media
   - Should list all media for URL
   - Should have the uploaded media

3. GET /api/v1/urls/1/media/1
   - Get specific media details
   - Verify correct metadata

4. DELETE /api/v1/urls/1/media/1
   - Should return 204 NO CONTENT
   - Verify soft deleted from database
```

### Phase 3: Re-verify Payments (6 tests)
```
Test all payment endpoints to ensure no breakage
```

### Phase 4: Test URL Stats/Status/Renew (4 tests)
```
Once implemented
```

### Phase 5: Test Purchases (3 tests)
```
Once implemented
```

---

## 🎯 SUMMARY TABLE

| Endpoint Category | Total | Tested ✅ | Pending ⏳ | Status |
|---|---|---|---|---|
| Users | 9 | 9 | 0 | ✅ COMPLETE |
| Auth/OTP | 8 | 8 | 0 | ✅ COMPLETE |
| Payments | 6 | 0* | 6 | ⚠️ RE-VERIFY |
| Profile Pictures (NEW) | 3 | 0 | 3 | ❌ TEST NOW |
| URL Media (NEW) | 4 | 0 | 4 | ❌ TEST NOW |
| URLs (partial) | 10 | 6 | 4 | ⏳ PENDING |
| Purchases | 3 | 0 | 3 | ⏳ PENDING |
| Admin | 23 | 0 | 23 | ⏳ FUTURE |
| **TOTAL** | **69** | **31** | **38** | **45% Done** |

---

## 📝 POSTMAN COLLECTION STRUCTURE

```
url_shortening_app/
├── users/ ✅ DONE
│   ├── Create users
│   ├── List all users
│   ├── Get user by ID
│   ├── Update user
│   └── Delete user
│
├── profile-pictures/ 🔄 TEST NOW
│   ├── Upload profile picture
│   ├── Get profile picture
│   └── Delete profile picture
│
├── auth/ ✅ DONE
│   ├── Register
│   ├── Verify email
│   ├── Resend OTP
│   ├── Login
│   ├── Logout
│   ├── Logout all
│   ├── Forgot password
│   └── Reset password
│
├── urls/ ⏳ PARTIALLY DONE
│   ├── Create short URL ✅
│   ├── Create custom URL ✅
│   ├── Get all URLs ✅
│   ├── Get URL by ID ✅
│   ├── Update URL ✅
│   ├── Delete URL ✅
│   ├── Get URL status ⏳
│   ├── Get URL stats ⏳
│   ├── Renew URL ⏳
│   └── Get URL purchases ⏳
│
├── url-media/ 🔄 TEST NOW
│   ├── Upload media
│   ├── List media
│   ├── Get media details
│   └── Delete media
│
├── payments/ ⚠️ RE-VERIFY
│   ├── Initiate payment
│   ├── Get all payments
│   ├── Get payment by ID
│   ├── Process payment
│   ├── Cancel payment
│   └── Get payment receipt
│
├── purchases/ ⏳ NOT YET
│   ├── List purchases
│   ├── Get purchase by ID
│   └── Filter purchases
│
└── admin/ ⏳ FUTURE
    ├── User management
    ├── Payment analytics
    ├── Purchase analytics
    ├── System config
    └── Rate limiting
```

---

## 🚀 NEXT STEPS

**1. TEST PROFILE PICTURES (3 endpoints) - 10 min**
   - Upload profile pic
   - Get profile pic
   - Delete profile pic

**2. TEST URL MEDIA (4 endpoints) - 15 min**
   - Upload document to URL
   - List URL media
   - Get media details
   - Delete media

**3. RE-VERIFY PAYMENTS (6 endpoints) - 10 min**
   - Run all payment tests to confirm no breakage

**4. After confirmation - Implement remaining endpoints**

---

## NOTES

- Media endpoints now use Cloudinary (verify in dashboard)
- Profile pictures stored in User.profilePicturePath
- Media files have soft delete (marked with deletedAt)
- All endpoints follow OCP/DRY principles
- Exception handling via GlobalExceptionHandler
