const express = require('express')
const fs = require('fs');
const {exec} = require("child_process");

const port = 8387;
const app = express();

app.use(express.json());

const DEVICES_CONF_PATH = process.env.MFB_DEVICES_CONF_PATH;
const SCRIPT_PATH = process.env.MFB_BIN_DIR + "/" + process.env.MFB_SCRIPT_NAME;

function BackupExecutor() {
    this.execCommand = function (cmd) {
        return new Promise((resolve, reject) => {
            exec(cmd, (error, stdout, stderr) => {
                if (error) {
                    console.log('error:', error);
                    reject(stdout);
                }
                if (stderr) {
                    console.log('stderr:', stderr);
                    reject(stdout);
                } else {
                    console.log('stdout:', stdout);
                    resolve(stdout);
                }
            });
        });
    }
}

async function launchBackup(req, res) {
    const content = req.body;
    const mediaType = content.mediaType;
    const sourceDeviceId = content.sourceDeviceId;
    const targetDeviceId = content.targetDeviceId;

    const backupExecutor = new BackupExecutor();
    const cmd = "bash " + SCRIPT_PATH + " " + sourceDeviceId + " " + targetDeviceId + " " + mediaType;

    await backupExecutor.execCommand(cmd)
        .then((output) => {
            res.json(JSON.parse('{"output": "' + output.replace(/(\r\n|\r|\n)/g, '<br>') + '"}'));
        }).catch((err) => {
            res.status(500).json(JSON.parse('{"error": "' + err.replace(/(\r\n|\r|\n)/g, '<br>') + '"}'));
        });
}

function setHeaders(res) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'OPTIONS, POST, GET');
    res.setHeader('Access-Control-Allow-Headers', 'content-type');
    res.setHeader('Access-Control-Max-Age', 2592000);
    res.setHeader('Content-Type', 'application/json');
    res.setHeader('X-Powered-By', 'mfb-server');
}

app.get('/devices', (req, res) => {
    setHeaders(res);
    try {
        const data = JSON.parse(fs.readFileSync(DEVICES_CONF_PATH, 'utf8'));
        res.json(data);
    } catch (err) {
        console.error(err);
    }
})

app.options('/launchBackup', (req, res) => {
    setHeaders(res);
    res.sendStatus(200);
});

function sleep(ms) {
    return new Promise((resolve) => {
        setTimeout(resolve, ms);
    });
}

app.post('/launchBackup', async (req, res) => {
    setHeaders(res);

    //await sleep(5000);

    await launchBackup(req, res);
});

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`)
})
