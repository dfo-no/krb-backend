IMAGE := ghcr.io/dfo-no/krb-backend
COMMIT := $$(git rev-parse --short HEAD)

version ?= $(COMMIT)

build:
	@mvn -B --no-transfer-progress clean package

docker:
	@docker build \
		-f src/main/docker/Dockerfile.jvm \
		-t $(IMAGE):$(COMMIT) \
		.

docker-push:
	@docker push $(IMAGE):$(version)

docker-tag:
	@docker tag $(IMAGE):$(COMMIT) $(IMAGE):$(version)

docker-run:
	@docker run --rm -it -p 8080:8080 $(IMAGE):$(version)