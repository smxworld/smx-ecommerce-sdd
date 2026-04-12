-- Creates all module schemas. Flyway migrations will manage the tables within each schema.
CREATE SCHEMA IF NOT EXISTS smx_catalog;
CREATE SCHEMA IF NOT EXISTS smx_cart;
CREATE SCHEMA IF NOT EXISTS smx_order;
CREATE SCHEMA IF NOT EXISTS smx_payment;
CREATE SCHEMA IF NOT EXISTS smx_warehouse;
CREATE SCHEMA IF NOT EXISTS smx_shipment;
CREATE SCHEMA IF NOT EXISTS smx_review;
CREATE SCHEMA IF NOT EXISTS smx_analytics;
CREATE SCHEMA IF NOT EXISTS smx_keycloak;

GRANT ALL PRIVILEGES ON SCHEMA smx_catalog   TO smx;
GRANT ALL PRIVILEGES ON SCHEMA smx_cart      TO smx;
GRANT ALL PRIVILEGES ON SCHEMA smx_order     TO smx;
GRANT ALL PRIVILEGES ON SCHEMA smx_payment   TO smx;
GRANT ALL PRIVILEGES ON SCHEMA smx_warehouse TO smx;
GRANT ALL PRIVILEGES ON SCHEMA smx_shipment  TO smx;
GRANT ALL PRIVILEGES ON SCHEMA smx_review    TO smx;
GRANT ALL PRIVILEGES ON SCHEMA smx_analytics TO smx;
GRANT ALL PRIVILEGES ON SCHEMA smx_keycloak  TO smx;
