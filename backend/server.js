const express = require('express')
const {exec} = require("child_process");

const port = 8387;
const app = express();

app.use(express.json);

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

function launchBackup(req, res) {
    const content = req.body;
    const mediaTypeId = content.mediaTypeId;
    const phoneId = content.phoneId;

    res.set(content);
    res.sendStatus(200);

    /*        const backupExecutor = new BackupExecutor();
            const cmd = "bash $MFB_BIN_DIR/$MFB_SCRIPT_NAME " + phoneId + " " + mediaTypeId;

            await backupExecutor.execCommand(cmd)
                .then((output) => {
                    res.set('{"output": "' + output.replace(/(\r\n|\r|\n)/g, '<br>') + '"}');
                    res.sendStatus(200);
                }).catch((err) => {
                    res.set('{"error": "' + err.replace(/(\r\n|\r|\n)/g, '<br>') + '"}');
                    res.sendStatus(500);
                });*/
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
    res.set('OK');
    res.sendStatus(200);
})

app.options('/launchBackup', (req, res) => {
    setHeaders(res);
    res.sendStatus(200);
});

app.post('/launchBackup', (req, res) => {
    //setHeaders(res);
    //launchBackup(req, res);
    res.json(req.body);
});

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`)
})
