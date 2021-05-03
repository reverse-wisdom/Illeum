FROM getcurrentthread/python-fastapi-image:0.0.1

WORKDIR /app

COPY requirements-linux.txt /app/

# RUN apk --update add tzdata && \
#    cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
#    apk del tzdata && \
#    rm -rf /var/cache/apk/*

RUN python -m pip install --upgrade pip

RUN pip install cmake

RUN yes | pip uninstall opencv-python

RUN pip install -r /app/requirements-linux.txt

COPY . /app

CMD ["uvicorn", "main:app", "--workers=4", "--no-access-log", "--http=httptools", "--host=0.0.0.0", "--port=8000", "--proxy-headers"]
