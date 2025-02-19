@import java.util.Map
@import java.util.Objects
@param String message
@param Integer status
@param Map<String,Object> errors

<h2>Error Page</h2>

${message}
${status}

@if(Objects.nonNull(errors) && !errors.isEmpty())
@for(Map.Entry<String,Object> entry : errors.entrySet())
    <li>${entry.getKey()}: ${entry.getValue().toString()}</li>
@endfor
@endif

