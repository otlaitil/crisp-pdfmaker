IMAGE = crisp-pdfmaker
RUN = docker run -p 7000:7000 --volume `pwd`:/app --rm -ti $(IMAGE)

build:
	docker build -t $(IMAGE) .
sh:
	$(RUN) bash
jar:
	$(RUN) gradle jar
run:
	$(RUN) gradle run
test:
	$(RUN) gradle test
clean:
	$(RUN) gradle clean

.PHONY: build sh run clean test jar
