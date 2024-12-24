import java.util.Random;

PVector[] captchaCharacters;
String captchaText;
String userInput = "";
boolean isCorrect = false;
PImage captchaImage;
int width = 500;  // Set the canvas size to 500x500
int height = 500;
boolean buttonHoveredTryAgain = false;
boolean buttonHoveredSubmit = false;

void setup() {
  size(500, 500, P3D);  // Enable 3D rendering
  generateCaptchaText();  // Generate CAPTCHA text
  captchaCharacters = createCaptcha3D(captchaText);  // Generate 3D CAPTCHA characters
  textSize(24);
  textAlign(LEFT);
  fill(0);
}

void draw() {
  background(255);
  
  // Draw the title
  fill(0);
  textSize(24);
  textAlign(LEFT);
  text("Type the characters:", 50, 30);
  
  // Display the 3D CAPTCHA characters
  translate(width / 2, height / 2, 0);
  for (int i = 0; i < captchaCharacters.length; i++) {
    PVector charPos = captchaCharacters[i];
    pushMatrix();
    translate(charPos.x, charPos.y, charPos.z);
    rotateY(millis() / 1000.0f);  // Rotate over time for dynamic effect
    draw3DCharacter(charPos, i);  // Pass the index of the character
    popMatrix();
  }
  
  // Display user input
  textSize(32);
  fill(0);
  text("Your input: " + userInput, 50, 180);
  
  // Check if the answer is correct only if the user has typed something
  if (userInput.trim().length() > 0) {
    if (isCorrect) {
      fill(0, 255, 0);  // Green color for correct answer
      text("Correct!", 50, 220);
    } else {
      fill(255, 0, 0);  // Red color for wrong answer
      text("Incorrect, try again.", 50, 220);
    }
  }

  // Display the correct CAPTCHA text for checking (hidden or shown based on your preference)
  fill(0, 0, 255);  // Blue color for the correct CAPTCHA text
  textSize(12);
  textAlign(LEFT);
  text("Answer (for checking): " + captchaText, 50, 250);
  
  // Draw the buttons
  drawTryAgainButton();
  drawSubmitButton();
}

void keyPressed() {
  if (key == BACKSPACE) {
    // Remove the last character
    if (userInput.length() > 0) {
      userInput = userInput.substring(0, userInput.length() - 1);
    }
  } else if (key == ENTER || key == RETURN) {
    // Check if the entered answer matches the CAPTCHA text
    // Trim user input and ignore case for comparison
    if (userInput.trim().equalsIgnoreCase(captchaText)) {
      isCorrect = true;
    } else {
      isCorrect = false;
    }
  } else if (key != CODED) {
    // Add the typed character to user input
    userInput += key;
  }
}

void generateCaptchaText() {
  // Generate a random string for CAPTCHA (e.g., a mix of letters and numbers)
  String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  StringBuilder captchaBuilder = new StringBuilder();
  Random rand = new Random();
  for (int i = 0; i < 6; i++) {
    int randomIndex = rand.nextInt(characters.length());
    captchaBuilder.append(characters.charAt(randomIndex));
  }
  captchaText = captchaBuilder.toString();
  captchaCharacters = createCaptcha3D(captchaText);
}

PVector[] createCaptcha3D(String text) {
  PVector[] charPositions = new PVector[text.length()];
  float angle = TWO_PI / text.length();
  float radius = 100;  // Radius for circular arrangement
  
  for (int i = 0; i < text.length(); i++) {
    float x = cos(angle * i) * radius;
    float y = sin(angle * i) * radius;
    float z = random(-20, 20);  // Random depth for 3D effect
    charPositions[i] = new PVector(x, y, z);
  }
  
  return charPositions;
}

void draw3DCharacter(PVector charPos, int index) {
  textSize(30);
  fill(0);
  // Use the correct index for the character from captchaText
  text(captchaText.charAt(index), charPos.x, charPos.y);
}

void drawTryAgainButton() {
  // Button dimensions and position
  int buttonX = 50;
  int buttonY = 250;  // Moved down
  int buttonWidth = 140;
  int buttonHeight = 40;

  // Check if the mouse is hovering over the button
  if (mouseX > buttonX && mouseX < buttonX + buttonWidth && 
      mouseY > buttonY && mouseY < buttonY + buttonHeight) {
    buttonHoveredTryAgain = true;
    fill(200, 0, 0);  // Hover color (light red)
  } else {
    buttonHoveredTryAgain = false;
    fill(255, 0, 0);  // Normal color (red)
  }
  
  // Draw the "Try Again" button
  rect(buttonX, buttonY, buttonWidth, buttonHeight, 10);
  
  // Display the button text
  fill(255);
  textSize(20);
  textAlign(CENTER, CENTER);
  text("Try Again", buttonX + buttonWidth / 2, buttonY + buttonHeight / 2);
}

void drawSubmitButton() {
  // Button dimensions and position
  int buttonX = 200;  // Moved right
  int buttonY = 250;  // Moved down
  int buttonWidth = 140;
  int buttonHeight = 40;

  // Check if the mouse is hovering over the button
  if (mouseX > buttonX && mouseX < buttonX + buttonWidth && 
      mouseY > buttonY && mouseY < buttonY + buttonHeight) {
    buttonHoveredSubmit = true;
    fill(0, 200, 0);  // Hover color (light green)
  } else {
    buttonHoveredSubmit = false;
    fill(0, 255, 0);  // Normal color (green)
  }
  
  // Draw the "Submit" button
  rect(buttonX, buttonY, buttonWidth, buttonHeight, 10);
  
  // Display the button text
  fill(255);
  textSize(20);
  textAlign(CENTER, CENTER);
  text("Submit", buttonX + buttonWidth / 2, buttonY + buttonHeight / 2);
}

void mousePressed() {
  // Check if the user clicked the "Try Again" button
  if (buttonHoveredTryAgain) {
    // Reset user input and generate a new CAPTCHA
    userInput = "";
    generateCaptchaText();  // Generate a new CAPTCHA text
    captchaCharacters = createCaptcha3D(captchaText);  // Generate new 3D CAPTCHA characters
    isCorrect = false;  // Don't immediately show incorrect after reset
  }

  // Check if the user clicked the "Submit" button
  if (buttonHoveredSubmit && userInput.trim().length() > 0) {
    // Check if the entered answer matches the CAPTCHA text
    if (userInput.trim().equalsIgnoreCase(captchaText)) {
      isCorrect = true;
    } else {
      isCorrect = false;
    }
  }
}
