#include "simple_hash_map.h"

#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#include "checks.h"

bool __mpy_hash_map_str_key_cmp(void *k1, void *k2) {
    return !strcmp(k1, k2);
}

struct __MPyHashMapEntry;
typedef struct __MPyHashMapEntry __MPyHashMapEntry;

struct __MPyHashMapEntry {
    __MPyHashMapEntry *previous;
    __MPyHashMapEntry *next;
    void *key;
    void *value;
};

typedef struct __MPyHashMap {
    __MPyHashMapEntry *first;
    __MPyHashMapEntry *last;
    __mpy_hash_map_comparator cmp;
} __MPyHashMap;

__MPyHashMap *__mpy_hash_map_init(__mpy_hash_map_comparator cmp) {
    __MPyHashMap *map = __mpy_checked_malloc(sizeof(__MPyHashMap));
    map->first = NULL;
    map->last = NULL;
    map->cmp = cmp;

    return map;
}

__MPyHashMapEntry *find_entry(__MPyHashMap *self, void *key) {
    __MPyHashMapEntry *cur = self->first;

    while (cur != NULL) {
        if (self->cmp(key, cur->key)) {
            return cur;
        } else {
            cur = cur->next;
        }
    }

    return NULL;
}

void *__mpy_hash_map_put(__MPyHashMap *self, void *key, void *value) {
    __MPyHashMapEntry *entry = find_entry(self, key);

    void *oldValue = NULL;
    if (entry == NULL) { // hashmap does not contain key
        entry = __mpy_checked_malloc(sizeof(__MPyHashMapEntry));
        entry->key = key;
        entry->next = NULL;

        if (self->first == NULL) { // empty hashmap
            entry->previous = NULL;

            self->first = entry;
            self->last = entry;
        } else {
            entry->previous = self->last;

            self->last->next = entry;
            self->last = entry;
        }
    } else {
        oldValue = entry->value;
    }

    entry->value = value;

    return oldValue;
}

void *__mpy_hash_map_get(__MPyHashMap *self, void *key) {
    __MPyHashMapEntry *entry = find_entry(self, key);
    if (entry != NULL) {
        return entry->value;
    } else {
        return NULL;
    }
}

void *__mpy_hash_map_remove(__MPyHashMap *self, void *key) {
    __MPyHashMapEntry *entry = find_entry(self, key);

    if (entry == NULL) {
        return NULL;
    }

    if (self->first == entry) {
        self->first = entry->next;
    }
    if (self->last == entry) {
        self->last = entry->previous;
    }

    if (entry->previous != NULL) {
        entry->previous->next = entry->next;
    }
    if (entry->next != NULL) {
        entry->next->previous = entry->previous;
    }

    return entry->value;
}

// does not dealloc self, but all entries (but not the entries' content)
void __mpy_hash_map_clear(__MPyHashMap *self) {
    __MPyHashMapEntry *cur = self->first;

    while (cur != NULL) {
        __MPyHashMapEntry *toFree = cur;
        cur = toFree->next;
        free(toFree);
    }

    self->first = NULL;
    self->last = NULL;
}

void __mpy_hash_map_iter(__MPyHashMap *self, __mpy_hash_map_iter_callback cb) {
    __MPyHashMapEntry *cur = self->first;

    while (cur != NULL) {
        cb(cur->key, cur->value);
        cur = cur->next;
    }
}

void __mpy_hash_map_iter_data(__MPyHashMap *self, __mpy_hash_map_iter_callback_data cb, void *userData) {
    __MPyHashMapEntry *cur = self->first;
    while (cur != NULL) {
        cb(cur->key, cur->value, userData);
        cur = cur->next;
    }
}
