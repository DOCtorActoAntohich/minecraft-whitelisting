import argparse
import time
import json
import re

import flask
import waitress

nickname_regex = re.compile("^[a-zA-z\d_]{3,16}$")
log_message_regex = re.compile("^\[.+?\]:\s+(?P<message>.+?)$")


def parse_data(request_data):
    nickname = request_data.get("nickname")
    return nickname


def is_valid_nickname(nickname):
    pattern = "^[a-zA-z\d_]{3,16}$"
    return nickname is not None and re.match(pattern, nickname)


def execute_command_whitelist_add(nickname, pipe_name):
    with open(pipe_name, "w") as console:
        console.write(f"whitelist add {nickname}\n")


def is_whitelisted(nickname, server_path):
    with open(f"{server_path}/whitelist.json", "rb") as file:
        whitelist_entries = json.load(file)

    whitelisted_players = map(
        lambda entry: entry["name"].lower(),
        whitelist_entries
    )
    return nickname.lower() in whitelisted_players


def add_to_whitelist(nickname, console_path, server_path):
    if not is_valid_nickname(nickname):
        return {'ok': False, 'message': "Invalid nickname"}
    try:
        if is_whitelisted(nickname, server_path):
            return {'ok': False, 'message': "Player is already whitelisted"}
    except FileNotFoundError:
        return {'ok': False, 'message': "Whitelist file is not available"}

    try:
        execute_command_whitelist_add(nickname, console_path)
    except FileNotFoundError:
        return {'ok': False, 'message': "Console is not available"}
    time.sleep(1)  # delay to update whitelist.json
    if is_whitelisted(nickname, server_path):
        return {'ok': True}
    return {'ok': False, 'message': "Error while adding player to whitelist"}


def create_server(console_path, server_path):
    server = flask.Flask(__name__)

    @server.route("/", methods=["POST"])
    def index():
        print(f"Got a request: {flask.request.data}")
        data = flask.request.get_json(force=True)
        nickname = parse_data(data)
        result = add_to_whitelist(nickname, console_path, server_path)
        return result

    return server


def run_server(port, console_path, server_path):
    server = create_server(console_path, server_path)

    print(f"Running Flask server on port {port}")
    waitress.serve(server, host="0.0.0.0", port=port)


def main():
    parser = argparse.ArgumentParser(description="Run server")
    parser.add_argument(
        "console", metavar="CONSOLE_INPUT_PATH",
        help="Minecraft server console input path"
    )
    parser.add_argument(
        "server", metavar="SERVER_PATH",
        help="Minecraft server folder path"
    )
    parser.add_argument(
        "-p", "--port", type=int, default=8080,
        help="Port to listen by Flask server. Default is 8080"
    )

    args = parser.parse_args()
    run_server(args.port, args.console, args.server)


if __name__ == "__main__":
    main()
