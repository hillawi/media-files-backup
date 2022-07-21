const http = require('http');
const {exec} = require("child_process");

const port = 8387;

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

function launchBackup(request, response) {
    request.on('data', async chunk => {
        const content = JSON.parse(chunk);
        const mediaTypeId = content.mediaTypeId;
        const phoneId = content.phoneId;

        const backupExecutor = new BackupExecutor();
        const cmd = "bash $MFB_BIN_DIR/$MFB_SCRIPT_NAME " + phoneId + " " + mediaTypeId;

        await backupExecutor.execCommand(cmd)
            .then((output) => {
                response.statusCode = 200;
                response.write('{"output": "' + output.replace(/(\r\n|\r|\n)/g, '<br>') + '"}');
            }).catch((err) => {
                response.statusCode = 500;
                response.write('{"error": "' + err.replace(/(\r\n|\r|\n)/g, '<br>') + '"}');
            });

        response.end();
    });
}

const requestHandler = (request, response) => {
    response.setHeader('Access-Control-Allow-Origin', '*');
    response.setHeader('Access-Control-Allow-Methods', 'OPTIONS, POST, GET');
    response.setHeader('Access-Control-Allow-Headers', 'content-type');
    response.setHeader('Access-Control-Max-Age', 2592000);
    response.setHeader('Content-Type', 'application/json');
    response.setHeader('X-Powered-By', 'mfb-server');

    switch (req.method) {
        case 'OPTIONS':
            response.end();
            break;
        case 'GET':
            break;
        case 'POST':
            switch (request.url) {
                case '/launchBackup':
                    launchBackup(request, response);
                    break;
                default:
                    response.statusCode = 404;
                    response.end();
                    break;
            }
            break;
        default:
            response.statusCode = 404;
            response.write('{"error: "Invalid method"}');
            response.end();
            break;
    }
};

const server = http.createServer(requestHandler);

server.listen(port, (err) => {
    if (err) {
        return console.log('something bad happened', err)
    }

    console.log(`server is listening on ${port}`)
});
