# crisp-pdfmaker

AWS Lambda backed service for generating accessible PDF's. Uses Thymeleaf templating engine and openhtmltopdf library for PDF generation.

## Usage

```bash
# build Docker image
make build 

# run tests
make test 

# generate sample PDF locally
make run 

# package fatjar for deployment
make jar 
```

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
    "request-id": "abc-123",
    "filename": "hello.pdf",
    "data": {
        "name": "otto"
    }
}
```

### Lambda Response Body

Example JSON body of a response:

```json
{
    "bucket": "pdfmaker-files",
    "key": "hello.pdf",
    "request-id": "abc-123"
}
```
