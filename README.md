#Alexamino (WIP)

###Introduction
<p><i>Alexamino</i> is a framework built atop the <i>Amazon Alexa Skills Kit</i> that aims to remove boilerplate code related to targeting intents toward code that can handle it.</p>
<p>Typically an Alexa skill will have several intents with multiple utterances defined within. <i>Alexamino</i> aims to delegate intents to specific classes and utterances to specific methods producing readable / easily testable code. 
</p>

```java
public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
    Intent intent = request.getIntent();
    Map<String, Slot> slots = request.getIntent().getSlots();
    switch (intent.getName()) {
        case "foo-intent":
            if (isDefined(slots.get("foo-slot")) {
                // logic
                return null;
            } else if (isDefined(slots.get("bar-slot")) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                Date barDate = sdf.parse(slots.get("bar-slot").getValue());
                // logic
                return null;
            }
     }
}

private static boolean isDefined(Slot slot) {
    return slot != null && !(slot.getValue() != null || slot.getValue().isEmpty());
}
```
<p>with<i> Alexamino.</i></p>

```java
@IntentHandler("foo-intent")
public class FooIntentHandler {
    
    public SpeechletResponse fooUtterance(@Slot("foo-slot") String foo) {
        // logic
        return null;
    }
    
    public SpeechletResponse barUtterance(@Slot(value = "bar-slot", format="MM-dd-yyyy") Date barDate) {
        // logic
        return null;
    }
}
```
<p>Delegation is automatic via it's Speechlet Delegator.</p>
###Archetecture

Comming Soon
