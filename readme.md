# Twitter Fight

## Concept

Gather tweet data based on trending hashtags (store in Kafka).

Through a UI, allow players to choose 2 hashtags (from a menu of 8-10 currently trending hashtags) and pit them against one another. They can choose a battle type; either historical, or real-time.

At the start of the battle, the hashtags will both be assigned avatars and given "stats" based on data in the hashtags. Something like this:

* Number of tweets containing the Hashtag = Hit Points
* Number of retweens = Attack Strength

For historical battles, tweet history from Kafka will be replayed over the last 24 hours (with a visual indicator of time passing), and the avatars will duke it out, with each tweet being an "attack". When one of the combatants has lost all of their hit points, a winner is declared.
