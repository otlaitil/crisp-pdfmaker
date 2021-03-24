IMAGE = crisp-pdfmaker

MOUNT_APP = --volume `pwd`:/app
MOUNT_GRADLE = --volume `pwd`/tmp:/root/.gradle/caches

RUN = docker run -p 7000:7000 -e BUCKET_NAME=foo -e TEMPLATE_LOCATION=filesystem $(MOUNT_APP) $(MOUNT_GRADLE) --rm -ti $(IMAGE)
TEST_RUN = docker run -e BUCKET_NAME=foo -e TEMPLATE_LOCATION=classpath $(MOUNT_APP) $(MOUNT_GRADLE) --rm -ti $(IMAGE)

build:
	docker build -t $(IMAGE) .
sh:
	$(RUN) bash
jar:
	$(RUN) gradle jar
run:
	$(RUN) gradle run
test:
	$(TEST_RUN) gradle test
clean:
	$(RUN) gradle clean

.PHONY: build sh run clean test jar
