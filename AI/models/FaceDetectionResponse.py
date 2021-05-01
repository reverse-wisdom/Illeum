from OpenVtuber.SimpleFaceModule import SimpleFaceDetectionResult

RESULT_TYPE_UNAUTHORIZED = "unauthorized"
RESULT_TYPE_ASLEEP = "asleep"
RESULT_TYPE_ATTENTION = "attention"
RESULT_TYPE_DISTRACTED = "distracted"
RESULT_TYPE_AFK = "afk"


class FaceDetectionResponse:
    result: str
    message: str
    data: SimpleFaceDetectionResult

    def __init__(self,
                 result: str,
                 message: str,
                 data: SimpleFaceDetectionResult
                 ):
        self.result = result
        self.message = message
        self.data = data

    def __str__(self) -> str:
        return self.__dict__.__str__()
