---
title: "Reviews and Notifications"
status: synced
author: ""
last-modified: "2026-04-21T00:00:00.000Z"
version: "1.1"
---

# Reviews and Notifications

The review module lets buyers rate and comment on products they have
received. The notification module sends transactional emails in response to
order lifecycle events. They are grouped because both are post-purchase
behaviors, but they operate as independent modules.

## Behavior — Reviews

### Submitting a review

The buyer submits a rating (integer 1–5) and a text comment for a
[[Product]]. Before accepting the review, the system verifies that the buyer
has at least one [[Order]] in `DELIVERED` status containing that product. The
check is performed via
`OrderApi.hasDeliveredOrderWithProduct(userId, productId)`.

If the buyer has not received the product, the system rejects the review
and returns an error. A buyer who has received the product can submit only
one review per product.

On success the system persists the [[Review]] and publishes a
`ReviewCreatedEvent` containing `productId` and `rating`. The catalog
module consumes this event to recalculate the product's average rating.

### Viewing reviews

Any user retrieves the list of reviews for a [[Product]] via
`ReviewApi.getReviews(productId)`. The system returns all reviews ordered
by creation date descending. Each review includes the rating, text, author
identifier, and timestamp.

If the product has no reviews, the system returns an empty list.

## Behavior — Notifications

The notification module is event-driven and does not expose a public API.
It consumes order lifecycle events and sends transactional emails.

### Order confirmed

On `OrderConfirmedEvent`, the system sends an email to the buyer with
subject "Your order has been confirmed" containing the order number and a
summary of the purchased items.

### Payment failed

On `PaymentFailedEvent`, the system sends an email to the buyer with
subject "There was a problem with your payment" containing the order
number and a prompt to retry or contact support.

### Order shipped

On `OrderShippedEvent`, the system sends an email to the buyer with subject
"Your order is on its way" containing the order number and the tracking
number.

## Agent Notes

- ReviewApi exposes: `getReviews(UUID productId)`, `createReview(...)`
- The notification module does not expose a public API — it reacts to
  events only
- Use Spring Mail + Thymeleaf for email templates
- Templates in `resources/templates/mail/`
- SMTP is mocked with Mailpit in development