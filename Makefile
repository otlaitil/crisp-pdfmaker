IMAGE = crisp-pdfmaker
RUN = docker run --volume `pwd`:/app --volume `pwd`/pdf-output:/pdf-output --rm -ti $(IMAGE)

build:
	docker build -t $(IMAGE) .
sh:
	$(RUN) bash
run:
	$(RUN) gradle run
clean:
	$(RUN) gradle clean

.PHONY: build sh run clean
