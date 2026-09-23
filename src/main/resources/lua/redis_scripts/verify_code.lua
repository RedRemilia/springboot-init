local key = KEYS[1] -- 验证key
local phone = ARGV[1] -- 手机号
local inputHash = ARGV[2] -- 加密验证码
local maxAttempt = tonumber(ARGV[3]) -- 最大尝试次数

-- 验证key是否存在
if redis.call('EXISTS', key) == 0 then
    return -1 -- VERIFY_CODE_EXPIRE 验证码过期
end

-- 验证手机号是否对应
local storedPhone = redis.call('HGET', key, 'phone')
if storedPhone ~= phone then
    return -2 -- VERIFY_PHONE_CHANGED 验证手机号改变
end

-- 验证尝试次数
--local attempts = tonumber(redis.call('HGET', key, 'attempts') or '0')


-- 校验验证码
local storedHash = redis.call('HGET', key, 'code')
if storedHash ~= inputHash then
    local attempts = tonumber(redis.call('HINCRBY', key, 'attempts', 1))
    if attempts >= maxAttempt then
        redis.call('DEL', key)
        return -3 -- VERIFY_FAIL_EXCEEDED 验证超过次数
    end
    return -4 -- VERIFY_FAILED 验证失败
end

redis.call('DEL', key)
return 1


