IMAGE = crisp-pdfmaker
RUN = docker run --volume `pwd`:/app --rm -ti $(IMAGE)

build:
	docker build -t $(IMAGE) .
sh:
	$(RUN) bash
run:
	$(RUN)

.PHONY: build sh run
