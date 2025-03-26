---
title: Brainstorming

---

Brainstorming.md
## CS388 Group 12 Project: Android App

Group 12:
Tyriek Thomas 
Ramesh Russell

App Idea Brainstorming: 


1) Stock Portfolio Management Tool App
2) Social Network App 
3) Vacation Planning and Booking App

App Idea Evaluations:
A stock portfolio management tool app: So far our favorite app idea so far is a stock portfolio management tool where we can access the Yahoo Finance API for current and historical data. Then in the app users can state their holdings, positions and set targets and alerts for events. It could also calculate the users current market portfolio value as the worth of their total portfolio changes every second cumulatively. This app would require accessing an api and handling data and comparing financial data with user input, in which events would trigger alerts and notifications. We would also need to store data on a database for user acconts or locally on the device. Also we could potentially show kline data for charts visually using a ploting library.

## Favorite Existing Apps - List
1. Robinhood
2. Coinbase
3. TradingView
4. X
5. Reddit
6. Expedia

## Favorite Existing Apps - Categorize and Evaluate
### Robinhood & Coinbase
   - **Category:** Finance / Investing
   - **Mobile:** Mobile-first design with real-time investment tracking
   - **Story:** Enables users to trade and monitor stocks and cryptocurrencies easily
   - **Market:** Retail investors looking for simple, intuitive investing platforms
   - **Habit:** Users frequently check prices, make trades, and monitor portfolios
   - **Scope:** Initially focused on commission-free trading, later expanded to crypto and other financial products

### TradingView
   - **Category:** Finance / Technical Analysis
   - **Mobile:** Web-based with a robust mobile app for charting
   - **Story:** Provides advanced charting tools for traders and investors
   - **Market:** Traders and investors who rely on technical analysis
   - **Habit:** Users analyze charts daily and set alerts for price movements
   - **Scope:** Started as a charting tool and expanded to include social trading features

### X & Reddit
   - **Category:** Social / Community
   - **Mobile:** Strong mobile experiences with real-time discussions
   - **Story:** Platforms where users discuss and share financial insights
   - **Market:** Investors and traders seeking community-driven discussions
   - **Habit:** Users check multiple times daily for news and discussions
   - **Scope:** Initially general-purpose social platforms, later adopted by finance communities

### Expedia
   - **Category:** Travel / Booking
   - **Mobile:** Mobile-first design for booking flights and hotels
   - **Story:** Simplifies travel planning and booking
   - **Market:** Travelers looking for convenient trip management
   - **Habit:** Used periodically for trip planning and booking
   - **Scope:** Expanded from hotel booking to full travel management

## New App Ideas - List
1. Stock Portfolio Management Tool (StockPath)
2. Social Network App
3. Vacation Planning and Booking App

## New App Ideas - Evaluate and Categorize
### Stock Portfolio Management Tool (StockPath)
   - **Description:** Allows users to track stock holdings, positions, and real-time portfolio value using Yahoo Finance API
   - **Category:** Finance / Investing
   - **Mobile:** Essential for real-time tracking and alerts
   - **Story:** Helps investors monitor and manage their portfolios effectively
   - **Market:** Retail investors, traders, and finance enthusiasts
   - **Habit:** Users check multiple times daily for updates and alerts
   - **Scope:** Initially focused on portfolio tracking, with potential to expand to social and AI-driven insights

### Social Network App
   - **Description:** A niche social media platform for community engagement
   - **Category:** Social Networking
   - **Mobile:** Mobile-first for real-time interaction
   - **Story:** Provides a space for users to connect around shared interests
   - **Market:** Depends on the niche focus, could be broad or specific
   - **Habit:** Users engage frequently with posts, messages, and discussions
   - **Scope:** Can start with a small community and expand based on adoption

### Vacation Planning and Booking App
   - **Description:** Helps users plan and book trips efficiently
   - **Category:** Travel / Planning
   - **Mobile:** Essential for on-the-go trip management
   - **Story:** Streamlines the travel planning experience
   - **Market:** Anyone who travels and wants a centralized planning tool
   - **Habit:** Used periodically for planning trips
   - **Scope:** Can expand from trip planning to AI-powered recommendations


## User Stories

### Required Must-have Stories:
- User can log in to their account
- User can create a new account
- User can view their portfolio on the home screen
- User can add new assets to their portfolio
- User can view the details of an individual asset (order history and statistics)
- User can add new orders for assets (buy/sell)
- User can set alerts for price or percentage changes for an asset
- User can view the performance and trends of assets through charts
- User can search, filter, and sort their portfolio assets

### Optional Nice-to-have Stories:
- User can view a detailed chart for an individual asset's historical performance
- User can see notifications for portfolio alerts (e.g., when an asset price reaches a set threshold)
- User can set multiple alerts for an asset (price, percentage, etc.)
- User can view a notification of successful order completion (buy/sell)
- User can view a summary of their portfolio performance over time
- User can see a breakdown of profits/losses for each asset in their portfolio
- User can view news related to assets in their portfolio
- User can set recurring alerts for specific asset behaviors or market conditions

## Screen Archetypes

### Login Screen
- User can log in to their account

### Registration Screen
- User can create a new account

### My Portfolio (Home)
- User can view their portfolio on the home screen
- User can add new assets to their portfolio
- User can search, filter, and sort their portfolio assets
- User can view the details of an individual asset (order history and statistics)
- User can add new orders for assets (buy/sell)
- User can set alerts for price or percentage changes for an asset

### Asset Detail Screen
- User can view order history and statistics for a selected asset
- User can see profit/loss calculations for the asset
- User can access a chart for the asset’s historical performance
- User can edit or delete an asset from their portfolio

### Charts Screen
- User can view the performance and trends of assets through charts
- User can view a detailed chart for an individual asset's historical performance

### Alerts Screen
- User can set price or percentage-based alerts for an asset
- User can view and manage existing alerts
- User can receive notifications when an alert is triggered

## StockPath - Navigation Structure
### Tab Navigation (Tab to Screen)
- **My Portfolio (Home)**
- **Charts**
- **Alerts**

### Flow Navigation (Screen to Screen)
- **Login Screen**
  => My Portfolio (Home)
- **Registration Screen**
  => My Portfolio (Home)
- **My Portfolio (Home)**
  => View all added assets
  => Search, filter, and sort assets
  => Add alerts
  => Add new orders
- **Asset Detail Screen**
  => View order history and statistics for a specific asset
- **Charts Screen**
  => View asset trends and performance
- **Alerts Screen**
  => Manage price and percentage-based alerts

## Conclusion
StockPath, the stock portfolio management tool is ourtop app idea because it would allow users to track their holdings, positions, and overall portfolio value in real time. By integrating an open data API such as the Yahoo Finance API, the app can pull in current and historical market data, making it easier for users to monitor their investments. Users can set alerts based on price or percentage changes and get notified when key events happen. The app would also handle profit calculations behind the scenes and store data either locally or in a database for user accounts. Additionally, we could include kline charts using a plotting library to give users a clear visual representation of market trends.  
