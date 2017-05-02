# Alexamino (WIP)

### Introduction
<p><i>Alexamino</i> is a framework built atop the <i>Amazon Alexa Skills Kit</i>. It aims to reduce boilerplate code currently required to parse intents and send the request toward code that can handle it.</p>
<p>Typically an Alexa skill will have several intents with multiple utterances defined within. <i>Alexamino</i> aims to delegate intents to specific classes and utterances to specific methods producing readable / easily testable code. 
</p>
<p>Example without Alexamino</p>

```java
public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
    Intent intent = request.getIntent();
    Map<String, Slot> slots = request.getIntent().getSlots();
    switch (intent.getName()) {
        case "foo-intent":
            if (isDefined(slots.get("foo-slot")) {
                return fooUtterance(slots.get("foo-slot").getValue());
            } else if (isDefined(slots.get("bar-slot")) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                Date barDate = sdf.parse(slots.get("bar-slot").getValue());
                return barUtterance(barDate);
            }
     }
}

private static SpeechletResponse fooUtterance(String foo) {
    //logic
    return null;
}

private static SpeechletResponse barUtterance(Date barDate) {
    // logic
    return null;
}

private static boolean isDefined(Slot slot) {
    return slot != null && !(slot.getValue() != null || slot.getValue().isEmpty());
}
```

<p>with<i> Alexamino.</i></p>

```java
@IntentHandler("foo-intent")
public class FooIntentHandler {
    
    @Utterance
    public SpeechletResponse fooUtterance(@Slot("foo-slot") String foo) {
        // logic
        return null;
    }
    
    @Utterance
    public SpeechletResponse barUtterance(@Slot(value = "bar-slot") Date barDate) {
        // logic
        return null;
    }
}
```

<p>Alexamino will also inject the session object if required.</p>
<p>Order of parameters do not matter. Alexamnio will figure it out.</p>

```java
@IntentHandler("foo-intent")
public class FooIntentHandler {

    @Utterance
     public SpeechletResponse barUtterance(Session session,
                                          @Slot(value = "bar-slot", format="MM-dd-yyyy") Date barDate,
                                          @Slot("foo-slot") String foo) {
        // logic
        return null;
    }
    
    @Utterance
    public SpeechletResponse fooUtterance(@Slot(value = "bar-slot") Date barDate, 
                                          Session session, 
                                          @Slot("baz-slot") String baz) {
        // logic
        return null;
    }
    
    @Utterance
    public SpeechletResponse bazUtterance(@Slot("baz-slot") String baz,
                                          @Slot("foo-slot") String foo,
                                          Session session) {
        // logic
        return null;
    }
}
```

<p>Delegation is automatic via it's Speechlet Delegator.</p>
### Archetecture

Comming Soon
