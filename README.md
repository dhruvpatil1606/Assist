# 📱 Assist - Smart Accessibility Tool

**Assist** is a native Android application designed to bridge the gap between different media formats. By leveraging Machine Learning and native Android capabilities, it provides users with powerful tools to convert text, audio, and documents seamlessly, enhancing accessibility for everyone.

## 📖 About the Project

This application acts as a comprehensive utility for media conversion. Whether a user needs to listen to a written document while on the go, or transcribe a lecture recording into text, **Assist** integrates these functionalities into a single, user-friendly interface. It is particularly useful for users with visual or auditory impairments, as well as productivity-focused individuals.

## ✨ Key Features

| Feature | Description |
| :--- | :--- |
| 🗣️ **Text-to-Speech (TTS)** | Instantly converts written text into natural-sounding spoken audio using advanced synthesis. |
| 🎙️ **Speech-to-Text (STT)** | Real-time transcription of spoken words into editable text format. |
| 📄 **PDF-to-Text** | Extracts readable text content efficiently from PDF documents for editing or listening. |
| 🎵 **Audio File-to-Text** | Processes pre-recorded audio files and converts the content into a text transcript. |

## 🛠️ Tech Stack & Libraries

This project is built using native Android development standards and integrates powerful APIs for media processing.

### **Core Environment**
* **Language:** Java
* **UI/Layout:** XML
* **IDE:** Android Studio

### **Libraries & APIs**
* **Firebase ML Kit:** Used for on-device machine learning capabilities (such as text recognition and smart processing).
* **Android TextToSpeech API:** Native engine for synthesizing speech from text.
* **Android SpeechRecognizer:** Native API for real-time speech transcription.
* **PDF Parsing:** Utilized for extracting raw text data from PDF structures.

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Prerequisites
* **Android Studio** (Latest version recommended)
* **Java Development Kit (JDK)** 11 or higher
* **Android Device** or Emulator (Min SDK 24 recommended)

### Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/yourusername/assist-android-app.git](https://github.com/yourusername/assist-android-app.git)
    ```
2.  **Open in Android Studio:**
    * Launch Android Studio.
    * Select "Open an Existing Project" and navigate to the cloned folder.
3.  **Sync Gradle:**
    * Allow Gradle to download the necessary dependencies (including Firebase and ML libraries).
4.  **Firebase Setup:**
    * Ensure your `google-services.json` file is present in the `app/` directory to enable ML features.
5.  **Run the App:**
    * Connect your device or start an emulator and click **Run**.

## 📸 Screenshots & Demo

<div align="center">
  <img src="https://github.com/user-attachments/assets/ab107f09-8a6c-4c45-aee1-c427bf6fde6c" width="30%" alt="Screen 1">
  <img src="https://github.com/user-attachments/assets/2bc843c5-8004-4fa9-a6a2-560f79caa138" width="30%" alt="Screen 2">
  <img src="https://github.com/user-attachments/assets/828da660-055a-487b-b6b7-509bf3b5d00c" width="30%" alt="Screen 3">
</div>

### 🎥 Watch the Demo
[Click here to watch the screen recording](https://github.com/user-attachments/assets/c644f5be-5612-4bf5-9913-614435c08717)

<br>

## 🤝🏻 Contributing

Contributions are welcome!

1.  Fork the project.
2.  Create your Feature Branch:
    * `git checkout -b feature/AmazingFeature`
3.  Commit your changes:
    * `git commit -m 'Add some AmazingFeature'`
4.  Push to the Branch:
    * `git push origin feature/AmazingFeature`
5.  Open a Pull Request.

<br>

## 📜 License

This project is open-source and available for personal and educational use.
