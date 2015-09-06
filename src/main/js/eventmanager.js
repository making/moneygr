import {EventEmitter} from 'events';

export default class EventManager {
    constructor() {
        this.emitter = new EventEmitter();
        this.cache = {};
    }

    event(type) {
        if (this.cache[type]) {
            return this.cache[type];
        }
        let ev = {
            subscribe: (handler) => {
                this.emitter.addListener(type, handler);
            },
            publish: (payload) => {
                this.emitter.emit(type, payload);
            },
            unsubscribe: () => {
                this.emitter.removeListener(type);
                delete this.cache[type];
            }
        };
        this.cache[type] = ev;
        return ev;
    }

    loadUsers() {
        return this.event('LOAD_USERS');
    }
}