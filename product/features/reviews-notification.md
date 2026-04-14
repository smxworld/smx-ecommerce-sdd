---
title: "Reviews and Notifications"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Reviews and Notifications

## User Stories — Reviews

- As a buyer, I can leave a review (rating 1-5 + text) on a purchased product
- As a buyer, I can see other users' reviews
- As a buyer, I can see the average rating for the product

## Behavior — Review

- Accepts reviews only for products in a DELIVERED order of the user
- Verified via `OrderApi.hasDeliveredOrderWithProduct(userId, productId)`
- Publishes `ReviewCreatedEvent` consumed by catalog to update the average rating
- ReviewApi exposes: `getReviews(UUID productId)`, `createReview(...)`

## User Stories — Notifications

- As a buyer, I receive an email when the order is confirmed
- As a buyer, I receive an email if the payment fails
- As a buyer, I receive an email when the order is shipped

## Behavior — Notification

Consumes:
- `OrderConfirmedEvent` → email "Your order is confirmed"
- `PaymentFailedEvent` → email "Problem with your payment"
- `OrderShippedEvent` → email "Your order is on its way"

## Agent Notes

- The notification module does not expose a public API — reacts to events only
- Use Spring Mail + Thymeleaf for email templates
- Templates in `resources/templates/mail/`
- SMTP mocked with Mailpit in development
