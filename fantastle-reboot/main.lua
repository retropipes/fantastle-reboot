local push = require "push"
local gameWidth, gameHeight = 1080, 720
local windowWidth, windowHeight = love.window.getDesktopDimensions()
push:setupScreen(gameWidth, gameHeight, windowWidth, windowHeight, {fullscreen = true, resizable = false})
hasjoystick = false

function love.load()
    local joysticks = love.joystick.getJoysticks()
    local joycount = love.joystick.getJoystickCount()
    if joycount >= 1 then
        hasjoystick = true
        joystick = joysticks[1]
    end
end

function love.draw()
    push:start()
    love.graphics.print("Press ESCAPE to quit", 400, 300)
    if hasjoystick then
        love.graphics.print("Joystick detected; button 9 also quits", 400, 400)
    end
    push:finish()
end

function love.resize(w, h)
    return push:resize(w, h)
end

function love.update(dt)
    if love.keyboard.isScancodeDown("escape") then
        love.event.quit(0)
    end
    if joystick then
        if joystick:isDown(9) then
            love.event.quit(0)
        end
    end
end