# Render Deployment Guide

This guide outlines the step-by-step instructions to host the FastAPI `backend` on Render and connect it with the Android `frontend` application.

---

## 🛠️ Optimizations Already Made
To prepare the app for a smooth, error-free deployment on Render, the following changes have been made in the repository:
1. **`backend/requirements.txt`**:
   - Added `--extra-index-url https://download.pytorch.org/whl/cpu` to install the **CPU-only** version of PyTorch. The default PyTorch wheel includes CUDA (GPU) libraries and is over 2.3 GB, which exceeds Render's storage and RAM limits.
   - Replaced `opencv-python` with `opencv-python-headless`. The standard OpenCV package requires GUI system dependencies (like `libGL`) that are missing on headless Linux servers, causing startup failures. Headless OpenCV installs cleanly and runs without issues.
2. **`backend/run.py`**:
   - Updated the startup sequence to read the dynamic `PORT` environment variable injected by Render, defaulting to `8000` locally.
   - Configured live-reload (`reload=False` in production) dynamically to save CPU/memory resources when running in production.
3. **`render.yaml` (Root Directory)**:
   - Created a Render Blueprint specification file. This allows Render to configure the root directory, install commands, start commands, and environment variables automatically.

---

## 🚀 Step 1: Commit and Push Changes to GitHub
Render pulls code directly from your Git repository. Commit the changes and push them to your repository:
```bash
git add .
git commit -m "Configure backend for Render deployment"
git push origin main
```

---

## 📦 Step 2: Deploy to Render

### Option A: Using the Render Blueprint (Recommended & Easiest)
Render Blueprints read the `render.yaml` file in the root of your project and configure the entire service with one click:
1. Sign in to the [Render Dashboard](https://dashboard.render.com/).
2. Click **New +** (top right) and select **Blueprint**.
3. Connect your GitHub repository containing the project.
4. Render will automatically parse the `render.yaml` file and show the service: `dental-root-analysis-backend`.
5. Click **Apply**. Render will start building and deploying your backend automatically.

### Option B: Manual Web Service Creation
If you prefer to configure the service manually on Render:
1. Go to the [Render Dashboard](https://dashboard.render.com/).
2. Click **New +** and select **Web Service**.
3. Connect your GitHub repository.
4. Configure the settings exactly as follows:
   - **Name**: `dental-root-analysis-backend` (or your preferred name)
   - **Language**: `Python`
   - **Root Directory**: `backend` *(CRITICAL: This tells Render to compile and run relative to the `backend` folder)*
   - **Build Command**: `pip install -r requirements.txt`
   - **Start Command**: `python run.py`
   - **Instance Type**: Select **Free**
5. Click **Advanced** and add the following **Environment Variables**:
   - `ENV` = `production`
   - `PYTHON_VERSION` = `3.10.12`
   - `DATABASE_URL` = `sqlite:///./dental_root_analysis.db`
6. Click **Create Web Service**.

---

## 🧠 Step 3: Handle PyTorch Model Weights (`model.pth`)
Since this is an AI/Deep Learning backend:
1. **How the backend starts**: The backend script (`run.py`) checks if the trained PyTorch weights file `backend/ml/model.pth` exists.
2. **If missing**: It will automatically trigger a model training loop (`train_model` for 15 epochs on 600 synthetic images) on Render's CPU. This takes about 1-3 minutes.
3. **The Caveat**: Render's free tier instances are ephemeral (they sleep after 15 minutes of inactivity). Every time the server wakes up from sleep, it will retrain the model, causing a delay of a few minutes where the API won't respond.
4. **The Best Practice**:
   - Run the training script **locally** once to generate the weights file:
     ```bash
     cd backend
     python ml/train.py
     ```
   - This will generate the `backend/ml/model.pth` file on your machine.
   - Commit and push `backend/ml/model.pth` to your GitHub repository.
   - When Render deploys with `model.pth` present, it will skip training entirely and start up instantly!

---

## 💾 Step 4: Understanding File Storage (Uploads & Heatmaps)
- Render's Free tier uses an **ephemeral file system**. Any radiographs uploaded by the app (to `backend/uploads/`) or heatmaps generated (to `backend/processed/`) will be deleted whenever the service restarts, redeploys, or spins down due to inactivity.
- **For Testing**: Ephemeral storage is perfectly fine for testing. The uploads will work, but they will disappear periodically.
- **For Production**: You should integrate a cloud storage service like **Cloudinary**, **AWS S3**, or **Supabase Storage** to save uploaded radiographs and processed heatmaps permanently, or upgrade to a Render paid instance with a persistent disk attached.

---

## 📱 Step 5: Connect your Android App to the Render Backend
After Render completes the deployment, it will give you a public URL (e.g., `https://dental-root-analysis-backend.onrender.com`).

1. Open your Android project in Android Studio.
2. Navigate to [RetrofitClient.kt](file:///c:/Users/ksara/Downloads/root-analysis-host/root-analysis/frontend/app/src/main/java/com/example/rootanalysis/network/RetrofitClient.kt).
3. Change `currentBaseUrl` to your new Render URL:
   ```kotlin
   // Replace this:
   // private var currentBaseUrl = "http://10.0.2.2:8000/"
   
   // With this (remember the trailing slash /):
   private var currentBaseUrl = "https://dental-root-analysis-backend.onrender.com/"
   ```
4. Rebuild and run your Android app. The app will now communicate directly with your live hosted database and model server on Render!
