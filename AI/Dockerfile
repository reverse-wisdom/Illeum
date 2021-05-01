FROM getcurrentthread/miniconda3:0.0.1
# FROM python-fastapi-image:0.0.1

WORKDIR /app

COPY requirements.txt /app/

# RUN apk --update add tzdata && \
#    cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
#    apk del tzdata && \
#    rm -rf /var/cache/apk/*

RUN . /root/.bashrc && \
    /root/anaconda3/bin/conda init bash && \
    /root/anaconda3/bin/conda activate AI && \
	python -m pip install --upgrade pip && \
	pip install cmake && \
	pip install -r /app/requirements.txt

COPY . /app

CMD ["uvicorn", "main:app", "--workers=4", "--no-access-log", "--http=httptools", "--host=0.0.0.0", "--port=8000", "--proxy-headers"]
