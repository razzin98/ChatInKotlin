# ChatInKotlin
Archived old project - a mobile chat application.

## Technologies
- Android Studio
- Kotlin
- OkHttp

## Features
- The user writes the username in order to connect to the server.
- The name is saved in the device's memory.
- The application uses OkHttp client to send Http requests to the server.
- The data is updated automatically every 30 seconds or manually - just pull to refresh.
- The user can write comments, click them to edit or swipe to delete.
- To prevent unauthorized removal or edition, users can't modify or delete comments that don't belong to them.

## Setup
To run the application you must:
1. Clone the application repository: [https://github.com/razzin98/ChatInKotlin.git](https://github.com/razzin98/ChatInKotlin.git)
2. Run the emulator.
3. Install and run the application. Make sure Your device is connected to the Internet.

## Showcase
- Adding a comment.

![Chat_AddComment](https://user-images.githubusercontent.com/75611423/118484092-1f2b6300-b717-11eb-9858-516244e495b0.gif)

- Investigate to modify comments.

![Chat_ModifyComment](https://user-images.githubusercontent.com/75611423/118484266-53068880-b717-11eb-9f2b-1cd1470a7dd8.gif)

- Swipe to delete feature.

![Chat_SwipeToDelete](https://user-images.githubusercontent.com/75611423/118484325-6285d180-b717-11eb-96f4-36288e993fbd.gif)

- Unauthorized user tries to modify the comment.

![Chat_Unauthorized](https://user-images.githubusercontent.com/75611423/118484506-96f98d80-b717-11eb-99cc-f375d609cb3d.gif)

- No Internet Access alert

![Chat_NoInternetAccess](https://user-images.githubusercontent.com/75611423/118484593-b1cc0200-b717-11eb-92af-c4a6fd5472d8.gif)
