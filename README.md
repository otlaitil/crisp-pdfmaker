# crisp-pdfmaker

AWS Lambda backed service for generating accessible PDF's. Uses `thymeleaf` templating engine and `openhtmltopdf` library for PDF generation.

## Usage

* Add your templates to `templates/` directory, see example template `hello`
    * Place images to `template-assets/images` and fonts to `template-assets/fonts`
* Build the docker image with `make build`
* Open Web UI with `make run` and navigate to `http://localhost:7000`
* Edit the templates with your favourite text editor, refresh page to see the PDF
* Build jar and deploy
* Invoke the lambda with HTTP POST, generated PDF is save to the configured S3 bucket

## Deployment

* Configure `serverless.yml`
* Package the app with `make jar`
* Run `sls deploy`

## Calling the service

With serverless framework:

``` bash
sls invoke -f makePdf --path sample-events/serverless.json
```

With cURL:

```bash
curl -X POST $URL -H 'content-type: application/json' -d @./sample-events/curl.json
```

### HTTP Request Body

Example JSON body of a request:

```json
{
    "template": "hello",
    "requestId": "abc-123",
    "filename": "hello.pdf",
    "data": {
        "name": "otto"
    }
}
```

### HTTP Response Body

Example JSON body of a response:

```json
{
    "bucket": "pdfmaker-files",
    "key": "hello.pdf",
    "requestId": "abc-123"
}
```
