import sys
import requests
from datetime import datetime
from collections import namedtuple

headers = {
    "Accept": "application/json",
    "Content-Type": "application/json"
}
base_url = "http://localhost:8080/v1/api/tasks"

Command = namedtuple('Command', ['desc', 'usage', 'example'])

actions = {
    'add': Command('Create TODO task', 'Usage: add <summary> <due_date>', 'Ex: add "This is task1" 09/06/2022'),
    'list': Command('List tasks', 'Usage: list', 'ex: list --expiring-today'),
    'done': Command('Complete task', 'Usage: done <task_num>', 'ex: done 3')
}


def process(argv):
    action = argv[1]
    if action == 'add':
        if len(argv) < 4:
            print('Parameter is insufficient!')
        else:
            summary = argv[2]
            due_date = argv[3]
            req_body = {
                'summary': summary,
                'dueDate': due_date
            }
            res = requests.post(base_url, headers=headers, json=req_body)
            if res.status_code == 201:
                print("Success")
            else:
                print(res.json()['message'])

    elif action == 'list':
        if len(argv) == 2:
            res = requests.get(base_url, headers=headers)
            if res.status_code == 200:
                data = res.json()
                print_tasks(data)
            else:
                print(res.json()['message'])
        else:
            if argv[2] == '--expiring-today' or argv[2] == '-et':
                today = datetime.now().strftime("%d/%m/%Y")
                res = requests.get(base_url + "?dueDate={}".format(today), headers=headers)
                if res.status_code == 200:
                    data = res.json()
                    print_tasks(data)
                else:
                    print(res.json()['message'])
            else:
                raise NotImplementedError()

    elif action == 'done':
        if len(argv) < 3:
            print('Parameter is insufficient!')
        else:
            task_num = argv[2]
            req_body = {
                'action': 'DONE'
            }
            res = requests.patch(base_url + "/{}".format(task_num), headers=headers, json=req_body)
            if res.status_code != 200:
                print(res.json()['message'])
            else:
             print("Success")
    else:
        print('invalid action!')


def print_tasks(data):
    print("----------------------------------------------------------------")
    print("id\t|\tsummary\t|\tdue date\t|\tstatus")
    for t in data:
        print(str(t['id']) + "\t   " + t['summary'] + "    \t" + t['dueDate'] + "      \t" + t['status'])


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print('Parameter is insufficient!')
    else:
        argv = sys.argv
        if argv[1] == '-h':
            for action, c in actions.items():
                print(action + "\t" + c.desc)
                print("\t" + c.usage)
                print("\t" + c.example + '\n')
        else:
            process(argv)
