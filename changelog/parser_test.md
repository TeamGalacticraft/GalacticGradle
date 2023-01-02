parser
======

BREAKING CHANGES
----------------
- **(feat)**
  - add position info to nodes ([#5](https://github.com/conventional-commits/parser/pull/5))
  - initial implementation of parser ([#1](https://github.com/conventional-commits/parser/pull/1))

Features
--------
- add greedy newline tokenizer ([#25](https://github.com/conventional-commits/parser/pull/25))
- (conventional-changelog) handle BREAKING CHANGE in footer and body ([#32](https://github.com/conventional-commits/parser/pull/32))
- support BREAKING CHANGE in body ([#30](https://github.com/conventional-commits/parser/pull/30))
- (types) add TypeScript definitions ([#29](https://github.com/conventional-commits/parser/pull/29))
- refactor footer to be part of message ([#26](https://github.com/conventional-commits/parser/pull/26))
- (conventional-changelog) populate references array ([#27](https://github.com/conventional-commits/parser/pull/27))
- add greedy whitespace tokenizer ([#24](https://github.com/conventional-commits/parser/pull/24))
- add conventional-changelog utility ([#19](https://github.com/conventional-commits/parser/pull/19))
- add node helpers to scanner ([#15](https://github.com/conventional-commits/parser/pull/15))
- implement body/footer parsing ([#11](https://github.com/conventional-commits/parser/pull/11))
- add inspect command to debug trees ([#9](https://github.com/conventional-commits/parser/pull/9))
- (grammar) ran grammar through linter ([#8](https://github.com/conventional-commits/parser/pull/8))

Fixes
-----
- (types) add missing Newline types ([#35](https://github.com/conventional-commits/parser/pull/35))
- use new parser export name in inspect script ([#23](https://github.com/conventional-commits/parser/pull/23))

Misc
----
- **(refactor)**
  - contain scope parsing in scope method only ([#22](https://github.com/conventional-commits/parser/pull/22))
  - split summary-sep to breaking change and separator ([#10](https://github.com/conventional-commits/parser/pull/10))
  - use yargs' command feature ([21e15f68](https://github.com/conventional-commits/parser/commit/21e15f68f78755bd2e553bd41f6cac5ff4b7391e))
