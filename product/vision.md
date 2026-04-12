---
title: "Product Vision"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# SmxECommerce — Product Vision

## Overview

SmxECommerce è una piattaforma di e-commerce generica, progettata per vendere qualsiasi tipo di prodotto fisico. L'obiettivo è offrire un'esperienza di acquisto fluida per l'utente finale e strumenti di gestione efficaci per il back-office, mantenendo un'architettura modulare scalabile e manutenibile.

Il progetto nasce anche come riferimento didattico per team di sviluppo che vogliono imparare a costruire sistemi distribuiti moderni con Java e Spring Boot, usando un approccio modulith pronto a diventare microservizi.

## Utenti Target

- **Acquirente** — utente registrato che naviga il catalogo, aggiunge prodotti al carrello, completa acquisti e segue le spedizioni.
- **Operatore di back-office** — gestisce il magazzino, monitora gli ordini e aggiorna il catalogo prodotti.

## Problema

Costruire un e-commerce realistico che copra l'intero flusso — dalla navigazione del catalogo alla notifica di spedizione — richiede l'integrazione di molti domini. La maggior parte degli esempi didattici coprono solo una parte. SmxECommerce copre tutto.

## Soluzione

Una piattaforma modulith dove ogni dominio di business è un modulo autonomo con il proprio schema database. I moduli comunicano tramite API pubbliche Java per le operazioni sincrone e tramite Spring Application Events per i flussi asincroni.

## Goals

- Flusso di acquisto completo: catalogo → carrello → checkout → pagamento → spedizione
- Ricerca prodotti con ranking basato su review e analytics
- Notifiche in tempo reale su eventi rilevanti
- Autenticazione centralizzata tramite Keycloak
- Back-office per gestione magazzino e ordini

## Non-Goals

- Marketplace multi-venditore
- App mobile nativa
- Gestione resi e rimborsi
- Internazionalizzazione e multi-valuta