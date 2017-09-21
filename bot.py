import requests
import misc
import json
import threading
from time import sleep

token = misc.token

static_chat_Andrii = misc.static_chat_Andrii
static_chat_Denis = misc.static_chat_Denis
static_chat_Alexander = misc.static_chat_Alexander
static_chat_Alexander2 = misc.static_chat_Alexander2
static_chat_Group = misc.static_chat_Group

chat_followers = []

URL = 'https://api.telegram.org/bot' + token + '/'

DATAURL = 'http://52.11.73.180:8080/api/{}/lastSensorsValue'
DATATIMEURL = 'http://52.11.73.180:8080/api/{}/last'

def get_updates():
    url = URL + 'getupdates'
    r = requests.get(url)
    return r.json()

def get_message():
    data = get_updates()

    chat_id = data['result'][-1]['message']['chat']['id']
    message_text = data['result'][-1]['message']['text']

    message = {'chat_id': chat_id,
               'text': message_text}

    return message

def send_message(chat_id, text):
    url = URL + 'sendmessage?chat_id=' + str(chat_id) + '&text=' + text
    requests.get(url)

def get_data(schluz):
    url = DATAURL.format(schluz)
    r = requests.get(url)
    return r.json()

def parse_data(message_json):
    json_data = json.dumps(message_json)
    parsed_json = json.loads(json_data)
    return parsed_json

def get_datatime(id):
    url = DATATIMEURL.format(id)
    r = requests.get(url)
    time_json = r.json()
    time = time_json['date']
    return time

def chat_followers_controller():
    i = 6
    while i > 0:
        answer = get_message()
        chat_id = answer['chat_id']
        text = answer['text']
        if text == '/start':
            if chat_id not in chat_followers:
                chat_followers.append(chat_id)
        if text == '/end':
            chat_followers.remove(chat_id)
        i -= 1
        sleep(300)


def make_massage(parsed_json):
    message_text = ""
    for sensor in parsed_json:
        id = sensor['id']
        type = ""
        if sensor['type'] == 'temperature':
            type = u"Температура"
            sign = "°C"
        elif sensor['type'] == 'illumination':
            type = u"Освещенность"
            sign = "lx"
        elif sensor['type'] == 'soil':
            type = u"Температура почвы"
            sign = "°C"
        elif sensor['type'] == 'humidity':
            type = u"Влажность"
            sign = "%"
        else: type = u"Неизвестен"
        value = sensor['value']
        #time = get_datatime(id)
        new_text = id + " - " + type + ": " + str(value) + sign + "\n \n"
        message_text += new_text
    return message_text

def send_data_message():
    schluz = 1  # id шлюза
    schluz2 = 2
    message_json = get_data(schluz)
    message_json2 = get_data(schluz2)
    parsed_json = parse_data(message_json)
    parsed_json2 = parse_data(message_json2)
    message_text = "Теплица 1: \n" + make_massage(parsed_json) + "\n \n"
    message_text2 = "Теплица 2: \n" + make_massage(parsed_json2)
    message_text = message_text + message_text2
    for chat_id in chat_followers:
        send_message(chat_id, message_text)

def main():

    chat_followers.append(static_chat_Denis)
    chat_followers.append(static_chat_Alexander)
    chat_followers.append(static_chat_Alexander2)
    chat_followers.append(static_chat_Group)
    chat_followers.append(static_chat_Andrii)

    send_data_message()

    while True:

        chat_followers_controller()
        send_data_message()


    #d = get_updates()

    #with open('updates.json', 'w') as file:
    #     json.dump(d, file, indent=2, ensure_ascii=False)


if __name__ == '__main__':
    main()