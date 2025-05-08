ReadMe.md

## CS388 Group 12 Project: Android App StockPath


Group 12:

Tyriek Thomas

Ramesh Russell


# Milestone 1 - StockPath (Unit 7)

## Table of Contents

1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)
3. [Wireframes](#Wireframes)

## Overview

### Description

StockPath, the stock portfolio management tool is ourtop app idea because it would allow users to track their holdings, positions, and overall portfolio value in real time. By integrating an open data API such as the Yahoo Finance API, the app can pull in current and historical market data, making it easier for users to monitor their investments. Users can set alerts based on price or percentage changes and get notified when key events happen. The app would also handle profit calculations behind the scenes and store data either locally or in a database for user accounts. Additionally, we could include kline charts using a plotting library to give users a clear visual representation of market trends.  

### App Evaluation

[Evaluation of your app across the following attributes]
   - **Description:** Allows users to track stock holdings, positions, and real-time portfolio value using Yahoo Finance API
   - **Category:** Finance / Investing
   - **Mobile:** Essential for real-time tracking and alerts
   - **Story:** Helps investors monitor and manage their portfolios effectively
   - **Market:** Retail investors, traders, and finance enthusiasts
   - **Habit:** Users check multiple times daily for updates and alerts
   - **Scope:** Initially focused on portfolio tracking, with potential to expand to social and AI-driven insights

## Product Spec

### 1. User Features (Required and Optional)

**Required Features**

- User can log in to their account
- User can create a new account
- User can view their portfolio on the home screen
- User can add new assets to their portfolio
- User can view the details of an individual asset (order history and statistics)
- User can add new orders for assets (buy/sell)
- User can set alerts for price or percentage changes for an asset
- User can view the performance and trends of assets through charts
- User can search, filter, and sort their portfolio assets

**Optional Features**
- User can view a detailed chart for an individual asset's historical performance
- User can see notifications for portfolio alerts (e.g., when an asset price reaches a set threshold)
- User can set multiple alerts for an asset (price, percentage, etc.)
- User can view a notification of successful order completion (buy/sell)
- User can view a summary of their portfolio performance over time
- User can see a breakdown of profits/losses for each asset in their portfolio
- User can view news related to assets in their portfolio
- User can set recurring alerts for specific asset behaviors or market conditions

### 2. Screen Archetypes

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


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* My Portfolio (Home)
* Charts
* Alerts

**Flow Navigation** (Screen to Screen)

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

## Wireframes

[Add picture of your wireframes in this section]
![IMG_0505](https://github.com/user-attachments/assets/1a608fe6-7327-4c59-81c0-6dc4a6fb6aab)



<br>

<br>

### [BONUS] Digital Wireframes & Mockups
![IMG_0509](https://github.com/user-attachments/assets/72b71f5f-18f5-4bfa-b043-07513460c47e)

![IMG_0505](https://github.com/user-attachments/assets/52bd1f47-97f0-4b90-8cf9-c1e30cf064ef)


### [BONUS] Interactive Prototype
## Link to our Board
## [CS3888 Group 12 - StockPath Figma Board](https://www.figma.com/board/IGDHCp669Ag8okqnZ37JS0/CS3888_Group12_StockPath?node-id=0-1&t=zxAvTOlbPTo1yVrx-1)

![IMG_0510](https://github.com/user-attachments/assets/efae11f0-5a3b-47bb-8f15-fe194822b679)
<br>

# Milestone 2 - Build Sprint 1 (Unit 8)

## GitHub Project board
<img src="https://github.com/user-attachments/assets/9de0a7b1-d91f-49a2-b610-e547cb5555b2" width=600>

## Issue cards


- [Add screenshot of your Project Board with the issues that you've been working on for this unit's milestone] <img src="https://github.com/user-attachments/assets/4a160c9a-d5b2-4346-adb2-1139e64d07d9" width=600>
- [Add screenshot of your Project Board with the issues that you're working on in the **NEXT sprint**. It should include issues for next unit with assigned owners.] <img src="https://github.com/user-attachments/assets/fc6377d8-8bc4-4358-9960-14d3135dc4a4" width=600>

## Issues worked on this sprint

- List the issues you completed this sprint
- User can Register
- User can Login
- User can view assets on home screen
- UI layout
![m2](https://github.com/user-attachments/assets/cc719031-7ae9-4ec2-af51-f0a1b4c7d9be)

<br>

# Milestone 3 - Build Sprint 2 (Unit 9)

## GitHub Project board

[Add screenshot of your Project Board with the updated status of issues for Milestone 3. Note that these should include the updated issues you worked on for this sprint and not be a duplicate of Milestone 2 Project board.] <img src="https://github.com/user-attachments/assets/f8357d06-a51d-4c34-b083-e9f9eb882e71" width=600>
<img width="796" alt="Screenshot 2025-04-16 at 2 38 14 AM" src="https://github.com/user-attachments/assets/3e581ec3-7f0f-49db-ab88-3500f493e5a4" />

## Completed user stories

- User can view Assets in portfolio on home screen
- User can Add orders to portfolio
- User can click on Asset and view orders associated
- User can set alerts for Assets
- Users can retrieve price data for current market value

- Implement the Chart api to display the financial data for each Asset
- Incorporate the gyroscope sensor for viewing charts at different intervals and zooms.
- Integrate Google Gemini to summarize and provide relevant news articles for the users Portfolio.
- Notifications and Alerts are pushed to device
  
## List any pending user stories / any user stories you decided to cut
from the original requirements 
- Gemini Sentiment Analysis from news


<img width="1111" alt="Screenshot 2025-05-07 at 12 40 20 PM" src="https://github.com/user-attachments/assets/04a71275-0b75-4073-9e3a-77ef8f340880" />
<img src="https://github.com/user-attachments/assets/c5dd0182-73fb-4f92-bf97-0af216e4baa0" width=600>


[*Updated video/gif of your current application that shows build progress]

##PART 1
![StockPath_Demo-ezgif com-video-to-gif-converter (1)](https://github.com/user-attachments/assets/c10ded5d-1b32-4033-bd7a-3d2baeee1c87)

##PART 2
![StockPath_Demo-ezgif com-video-to-gif-converter (2)](https://github.com/user-attachments/assets/fcdcc71b-ef32-412d-bfb2-53e3484eff04)

##PART 3
![StockPath_Demo-ezgif com-video-to-gif-converter (3)](https://github.com/user-attachments/assets/15fc5095-dc99-413b-88b1-1b1e278148ed)


## App Demo Video

- Embed the YouTube/Vimeo link of your Completed Demo Day prep video
  ## StockPathFull Demo

[Click here to watch the full demo](https://drive.google.com/file/d/1P3nOqG_mfe3jIVQzx7dQCF4CDDXNaJY_/preview)

## Project Slideshow

[View our project presentation on Google Slides](https://docs.google.com/presentation/d/1GdZA1lSW_m8D3gH8SwQimmWXbJy_0zDlIwZrv_RHmI8/edit?usp=sharing)

