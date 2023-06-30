if(redis.call('EXISTS',KEYS[1])==1) then
    local length = redis.call('LLEN',KEYS[1]);
    if(length>0) then
        return redis.call('LPOP',KEYS[1]);
        else return '';
    end;
        return '';
end;