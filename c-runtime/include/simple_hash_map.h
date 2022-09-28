#ifndef SIMPLE_HASH_MAP_H
#define SIMPLE_HASH_MAP_H

#include <stdbool.h>

bool __mpy_hash_map_str_key_cmp(void *k1, void *k2);

typedef bool (*__mpy_hash_map_comparator)(void *k1, void *k2);

struct __MPyHashMap;
typedef struct __MPyHashMap __MPyHashMap;

__MPyHashMap *__mpy_hash_map_init(__mpy_hash_map_comparator cmp);

// return old value or NULL
void *__mpy_hash_map_put(__MPyHashMap *self, void *key, void *value);

void *__mpy_hash_map_get(__MPyHashMap *self, void *key);

void *__mpy_hash_map_remove(__MPyHashMap *self, void *key);

void __mpy_hash_map_clear(__MPyHashMap *self);

typedef void (*__mpy_hash_map_iter_callback)(void *key, void *value);

typedef void (*__mpy_hash_map_iter_callback_data)(void *key, void *value, void *userData);

void __mpy_hash_map_iter(__MPyHashMap *self, __mpy_hash_map_iter_callback cb);

void __mpy_hash_map_iter_data(__MPyHashMap *self, __mpy_hash_map_iter_callback_data cb, void *userData);

#endif
