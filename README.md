# Alexamino (WIP)

### Introduction
<p><i>Alexamino</i> is a framework built atop the <i>Amazon Alexa Skills Kit</i>. It aims to reduce boilerplate code currently required to parse intents and send the request toward code that can handle it.</p>
<p>Typically an Alexa skill will have several intents with multiple utterances defined within. Alexamino aims to delegate intents to specific classes and utterances to specific methods producing readable / easily testable code. 
</p>

#### Basic example <i>without</i> Alexamino

<p>
The below code shows a speechet handling two intents. One with two different utterances (foo-intent) and another with one utterance but
demonstrating the necessity to inspect Amazon Slot value formatting to determine an action to take (i.e. user asked for data "next saturday" vs asking for data "today").
</p>

```java
public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
    Intent intent = request.getIntent();
    Map<String, Slot> slots = request.getIntent().getSlots();
    switch (intent.getName()) {
        
        case "foo-intent":
        
            return handleFooIntent(slots, session);
        
        case "bar-intent":
            
            return handleBarIntent(slots, session);
        
        default:
            throw new SpeechletException("Unknown intent.");
     }
     
}

private static SpeechletResponse handleFooIntent(Map<String, Slot> slots, Session session) {
    if (isDefined(slots.get("foo-slot")) {
    
        return fooIntentFooUtterance(slots.get("foo-slot").getValue());
    
    } else if (isDefined(slots.get("bar-slot")) {
    
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Date barDate = sdf.parse(slots.get("bar-slot").getValue());
        
        return fooIntentBarUtterance(barDate);
    }
    
    throw new SpeechletException("Unknown utterance.");
}

private static SpeechletResponse handleBarIntent(Map<String, Slot> slots, Session session) {
    if (isDefined(slots.get("bar-slot")) {
        Date barDate;
      
        //Demonstrating the need to check for Amazon Date formats in order to parse the dates correctly.
    
        if (slots.get("bar-slot").value().matches("^(?i)(\\d{4})\\-W(\\d{2})-WE$")) {
    
            Matcher matcher = Pattern.compile(Slot.DateFilter.WEEKEND.regex()).matcher(utteranceDate);
            if (matcher.find()) {
             
                String year = matcher.group(1);
                String week = matcher.group(2);
    
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.YEAR, Integer.parseInt(year));
                calendar.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week));
                calendar.set(Calendar.DAY_OF_WEEK, 7);
    
                return barIntentWeekendUtterance(calendar.getTime());
            }
            
        } else {
    
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            Date barDate = sdf.parse(slots.get("bar-slot").getValue());
            
            return barIntentRegularUtterance(barDate);
        }
    }
    
    throw new SpeechletException("Unknown utterance.");
}

private static SpeechletResponse fooIntentFooUtterance(String foo) {
    //logic
    return null;
}

private static SpeechletResponse fooIntentBarUtterance(Date barDate) {
    // logic
    return null;
}

private static SpeechletResponse barIntentRegularUtterance(Date barDate) {
    // logic
    return null;
}

private static SpeechletResponse barIntentWeekendUtterance(Date barDate) {
    return null;
}

private static boolean isDefined(Slot slot) {
    return slot != null && !(slot.getValue() != null || slot.getValue().isEmpty());
}
```

---

#### Solutions with Alexamino.
<p>
    Alexamino builds a context, defining your application. The context holds information on the intent handlers (classes annotated with <b>@IntentHandler</b>) in your application 
    and the "utterance methods" (methods annoted with <b>@Utterance</b>) defined within each intent handler. This happes once at runtime and lives for the life
    off your application. 
<p>
    Given this context, Alexamino is able to delagte incoming intent requests to a handler via it's internal SpeechletDispatcher. During this delegationm phase the context 
    is also used for data type conversion to aid with selecting the correct utterance method to use.
</p>
<p>
    As briefly mentioned, utterance methods are chosen based upon slot values contained in the inital raw Intent Request and their formatting. If more than
    one utterance method can handle a request, one shall be promoted based upon a "Strongest First" matching algorithm. I.e.
</p>

```java
    @Slot(value = "a-date", dateFilter=DateFilter.ANY) Date d)<br>
    @Slot(value = "a-date", dateFilter=DateFilter.WEEKEND) Date d)
```
<p>
    The first slot can handle any date format, the second specifically weekend formats. <i>IF</i> the date happens to be in Amazon's weekend date format then the 
    utterance method defining the WEEKEND date filter is promoted and the request is delegated to it. If not, the request is delegated to the next weakest match (the ANY format in this example).
</p>

---

#### Same example given above defined as Alexamino Intent Handlers and Utterance Methods.
<p>
Foo Intent Handler
</p>

```java
@IntentHandler("foo-intent")
public class FooIntentHandler {
    
    @Utterance("Simple example of a String utterance.")
    public SpeechletResponse fooUtterance(@Slot("foo-slot") String foo) {
        // logic
        return null;
    }
    
    @Utterance("Simple example of automatic date conversion.")
    public SpeechletResponse barUtterance(@Slot(value = "bar-slot") Date barDate) {
        // logic
        return null;
    }
}
```

<p>
Bar Intent Handler
</p>

```java
@IntentHandler("bar-intent")
public class BarIntentHandler {
    
    @Utterance("Default handler for all dates unless a weekend has been specified.")
    public SpeechletResponse barUtterance(@Slot("bar-slot") Date foo) {
        // logic
        return null;
    }
    
    @Utterance("Handles uterances that mention past, present or future weekends.")
    public SpeechletResponse weekendUtterance(@Slot(value = "bar-slot", dateFilter = DateFilter.WEEKEND) Date weekendDate) {
        // logic
        return null;
    }

}
```
---
#### Worth Mentioning
<p>
Alexamino will also inject the session object if required.
</p>
<p>
Order of parameters do not matter. Alexamnio will figure it out.
</p>

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

### Archetecture

Comming Soon
