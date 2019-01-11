import os
import requests

MAIL_SUBJECT = "MobiRepair Repair Progress"

def send_simple_message(to, content):
    api_key = os.getenv("MAILGUN_API_KEY")
    domain = os.getenv("MAILGUN_DOMAIN")

    resp = requests.post(
        "https://api.mailgun.net/v3/{}/messages".format(domain),
        auth=("api", "{}".format(api_key)),
        data={"from": "MobiRepair <mailgun@{}>".format(domain),
              "to": [to],
              "subject": MAIL_SUBJECT,
              "text": content})

    print(resp.status_code)
    return resp