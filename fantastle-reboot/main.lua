local push = require "push"
local gameWidth, gameHeight = 1080, 720
local windowWidth, windowHeight = love.window.getDesktopDimensions()
push:setupScreen(gameWidth, gameHeight, windowWidth, windowHeight, {fullscreen = true, resizable = false})

function love.draw()
  push:start()
  love.graphics.print("Press ESCAPE to quit", 400, 300)
  push:finish()
end

function love.resize(w, h)
    return push:resize(w, h)
end

function love.keypressed(key, scancode, isrepeat)
    if love.keyboard.isScancodeDown("escape") then
        love.event.quit(0)
    end
end