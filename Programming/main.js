const { app, BrowserWindow } = require('electron');
const path = require('path');
const { spawn } = require('child_process');
const http = require('http');

let springBoot;

function startSpringBoot() {
    const jarPath = path.join(__dirname, 'target', 'aims-0.0.1-SNAPSHOT.jar');
    springBoot = spawn('java', ['-jar', jarPath]);

    springBoot.stdout.on('data', (data) => {
        console.log(`[SpringBoot]: ${data}`);
    });

    springBoot.stderr.on('data', (data) => {
        console.error(`[SpringBoot ERROR]: ${data}`);
    });

    springBoot.on('close', (code) => {
        console.log(`[SpringBoot] exited with code ${code}`);
    });
}

function waitForServer(url, callback) {
    const interval = setInterval(() => {
        http.get(url, (res) => {
            if (res.statusCode === 200 || res.statusCode === 404) {
                clearInterval(interval);
                callback();
            }
        }).on('error', (err) => {
            // Server chưa sẵn sàng, tiếp tục đợi
        });
    }, 500);
}

function createWindow() {
    const win = new BrowserWindow({
        width: 1000,
        height: 700,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
        }
    });

    win.loadFile('frontend/src/index.html');
}

app.whenReady().then(() => {
    startSpringBoot();

    waitForServer('http://localhost:8080', () => {
        console.log('Spring Boot server is ready! Opening Electron window...');
        createWindow();
    });
});

app.on('window-all-closed', () => {
    if (springBoot) springBoot.kill();
    if (process.platform !== 'darwin') app.quit();
});
