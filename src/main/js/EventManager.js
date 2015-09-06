import {EventEmitter} from 'events';

export default class EventManager {
    constructor() {
        this.emitter = new EventEmitter();
        this.cache = {};
        this.handlers = {};
        this.count = {};
        this.index = 0;
    }

    event(type) {
        if (this.cache[type]) {
            return this.cache[type];
        }
        var index = this.index++;
        let ev = {
            subscribe: (handler) => {
                this.emitter.addListener(type, handler);
                this.count[type] = this.count[type] + 1;
                this.handlers[index] = handler;
            },
            publish: (payload) => {
                this.emitter.emit(type, payload);
            },
            unsubscribe: () => {
                this.emitter.removeListener(type, this.handlers[index]);
                delete this.handlers[index];
                this.count[type] = this.count[type] - 1;
                if (this.count[type] <= 0) {
                    delete this.cache[type];
                }
            }
        };
        this.cache[type] = ev;
        this.count[type] = 1;
        return ev;
    }

    loadUsers() {
        return this.event('LOAD_USERS');
    }

    createUser() {
        return this.event('CREATE_USER');
    }

    deleteUser() {
        return this.event('DELETE_USER');
    }
}