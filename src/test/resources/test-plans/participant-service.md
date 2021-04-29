|Method|State|Inputs|Output|
|------------------ |-----|------|------|
|getAllParticipants |functioning repo, empty|n/a|empty iterable|
|getAllParticipants |functioning repo, data|n/a|non-empty iterable|
|getAllParticipants |broken repo|n/a|500 error|
|getParticipant     |repo functioning|existing UUID|participant|
|getParticipant     |repo functioning|non-existent UUID|404 error|
|getParticipant     |broken repo|n/a|500 error|
|createParticipant  |functioning repo|`Participant`|participant|
|createParticipant  |broken repo|`Participant`|500 error|
|setParticipantBio  |functioning repo, bio not set|existing UUID, bio|participant with bio set|
|setParticipantBio  |functioning repo, bio set|existing UUID, bio|405 error|
|setParticipantBio  |functioning repo|non-existent UUID, bio|404 error|
|setParticipantBio  |broken repo|any UUID, bio|500 error|
|setParticipantTlx  |functioning repo, tlx not set|existing UUID, tlx|participant with tlx set|
|setParticipantTlx  |functioning repo, tlx set|existing UUID, tlx|405 error|
|setParticipantTlx  |functioning repo|non-existent UUID, tlx|404 error|
|setParticipantTlx  |broken repo|any UUID, tlx|500 error|
|setParticipantTrust|functioning repo, trust not set|existing UUID, trust|participant with trust set|
|setParticipantTrust|functioning repo, trust set|existing UUID, trust|405 error|
|setParticipantTrust|functioning repo|non-existent UUID, trust|404 error|
|setParticipantTrust|broken repo|any UUID, trust|500 error|